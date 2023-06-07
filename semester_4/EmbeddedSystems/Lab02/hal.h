#define PERIPH_BASE 0x40000000UL
#define AHB1PERIPH_BASE (PERIPH_BASE + 0x00020000UL)

/**
 * GPIO Bit SET and Bit RESET enumeration
 */
typedef enum {
	GPIO_PIN_RESET = 0,
	GPIO_PIN_SET
} GPIO_PinState;

/* !< AHB1 peripherals > */
#define GPIOA (AHB1PERIPH_BASE + 0x0000UL)
#define GPIOB (AHB1PERIPH_BASE + 0x0400UL)
#define GPIOC (AHB1PERIPH_BASE + 0x0800UL)
#define GPIOD (AHB1PERIPH_BASE + 0x0C00UL)
#define GPIOE (AHB1PERIPH_BASE + 0x1000UL)

#define GPIO_PIN_0     (0x0001)    /* Pin 0 is selected  */
#define GPIO_PIN_1     (0x0002)    /* Pin 1 is selected  */
#define GPIO_PIN_2     (0x0004)    /* Pin 2 is selected  */
#define GPIO_PIN_3     (0x0008)    /* Pin 3 is selected  */
#define GPIO_PIN_4     (0x0010)    /* Pin 4 is selected  */
#define GPIO_PIN_5     (0x0020)    /* Pin 5 is selected  */
#define GPIO_PIN_6     (0x0040)    /* Pin 6 is selected  */
#define GPIO_PIN_7     (0x0080)    /* Pin 7 is selected  */
#define GPIO_PIN_8     (0x0100)    /* Pin 8 is selected  */
#define GPIO_PIN_9     (0x0200)    /* Pin 9 is selected  */
#define GPIO_PIN_10    (0x0400)    /* Pin 10 is selected */
#define GPIO_PIN_11    (0x0800)    /* Pin 11 is selected */
#define GPIO_PIN_12    (0x1000)    /* Pin 12 is selected */
#define GPIO_PIN_13    (0x2000)    /* Pin 13 is selected */
#define GPIO_PIN_14    (0x4000)    /* Pin 14 is selected */
#define GPIO_PIN_15    (0x8000)    /* Pin 15 is selected */

void HAL_GPIO_TogglePin(uint gpio_port, uint pin);
GPIO_PinState HAL_GPIO_ReadPin(uint gpio_port, uint pin);
void HAL_GPIO_WritePin(uint gpio_port, uint pin, GPIO_PinState pin_state);
void HAL_Delay(uint delay_ms);

#endif
