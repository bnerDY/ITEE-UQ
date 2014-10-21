/*
 * joystick.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#ifdef AVR
#include <avr/io.h>

uint16_t JoystickX;
uint16_t JoystickY;
uint8_t JoystickButtons;

void init_joystick(void) {
	/* Set data direction register appropriately. The following
	 * bits are configured to be outputs:
	 * SS, MOSI, SCK (i.e. PB0, PB2, PB1)
	 */
	DDRB |= 0x07;

	/* Take the slave select line high */
	PORTB |= 0x01;

	/* Setup SPI Control Register (SPCR) and SPSR
	 * We set as follows:
	 * - SPE bit = 1 (SPI Enable)
	 * - MSTR bit = 1 (Enable Master Mode)
	 * - CPOL and CPHA are 0 (SPI mode 0)
	 * - SPR1,SPR0 = 01 with SPI2X = 1 (in SPSR register)
	 *		 (Clock / 8, i.e. 1MHz)
	 */
	SPCR = (1<<SPE)|(1<<MSTR)|(1<<SPR0);
	SPSR  = (1<<SPI2X);
}

static void delay_microsecond(void) {
	asm("nop\r\n" "nop\r\n" "nop\r\n" "nop\r\n"::);
}

static uint8_t transfer_byte(uint8_t byte) {
	/* Write out byte to SPDR register. This will initiate
	 * the transfer. We then wait until the most significant
	 * bit of SPSR (SPIF bit) is set - this indicates that
	 * the transfer is complete. (The final read of SPSR
	 * (after the MSB is 1) followed by a read of SPDR will
	 * cause the SPIF bit to be reset to 0. See page 169
	 * of the ATmega64 datasheet.)
	 */
	SPDR = byte;
	while((SPSR & 0x80) == 0) {
		;
	}
	return SPDR;
}

void check_joystick(uint8_t led1, uint8_t led2) {
	uint8_t Xlow, Xhigh, Ylow, Yhigh;
	uint8_t i;
	uint8_t cmd;

	/* Take SS (slave select) line (bit 0 of port B) low */
	PORTB &= 0xFE;

	/* Wait 15 microseconds */
	for(i=0; i<15; i++) {
		delay_microsecond();
	}

	/* Construct command word */
	cmd = 0x80;
	if(led1) {
		cmd |= 0x01;
	}
	if(led2) {
		cmd |= 0x02;
	}

	Xlow = transfer_byte(cmd);

	/* Wait for 10 microseconds */
	for(i=0; i<10; i++) {
		delay_microsecond();
	}

	Xhigh = transfer_byte(0);

	/* Wait for 10 microseconds */
	for(i=0; i<10; i++) {
		delay_microsecond();
	}

	Ylow = transfer_byte(0);

	/* Wait for 10 microseconds */
	for(i=0; i<10; i++) {
		delay_microsecond();
	}

	Yhigh = transfer_byte(0);

	/* Wait for 10 microseconds */
	for(i=0; i<10; i++) {
		delay_microsecond();
	}

	JoystickButtons = transfer_byte(0);

	/* Take SS line high again */
	PORTB |= 0x01;

	JoystickX = (Xhigh << 8) | Xlow;
	JoystickY = (Yhigh << 8) | Ylow;
}
#endif
