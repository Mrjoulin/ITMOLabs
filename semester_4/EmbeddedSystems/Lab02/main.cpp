#include "hal.h"

const int START_DELAY = 500, T = 50;
const char NUM_FRAMES = 8, NUM_LEDS = 8, NUM_SW = 4;
char N, FRAME = 0;

unsigned int LEDS_PINS[NUM_LEDS] = {
    GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5,
    GPIO_PIN_6, GPIO_PIN_8, GPIO_PIN_9,
    GPIO_PIN_11, GPIO_PIN_12
};
unsigned int SW_PINS[NUM_SW] = {GPIO_PIN_4, GPIO_PIN_8, GPIO_PIN_10, GPIO_PIN_12};


char read_sw() {
    char cur_n = 0;
    for (int i = 0; i < NUM_SW; ++i) {
        cur_n += HAL_GPIO_ReadPin(GPIOE, SW_PINS[i]) << i;
    }
    return cur_n;
}

void TIM6_IRQ_Handler() {
    __disable_irq();

    // animate
    HAL_GPIO_TogglePin(GPIOD, LEDS_PINS[(NUM_LEDS / 2 + FRAME) % NUM_LEDS]);
    HAL_GPIO_TogglePin(GPIOD, LEDS_PINS[(NUM_LEDS + (NUM_LEDS - 1) / 2 - FRAME) % NUM_LEDS]);
    FRAME = (FRAME + 1) % NUM_FRAMES;

    // Update timer 
    N = read_sw();
    WRITE_REG(TIM6_ARR, START_DELAY + N * T);

    __enable_irq();
}

int umain() {
    registerTIM6_IRQHandler(TIM6_IRQ_Handler);

    __enable_irq();

    // First frame will be shown immediately (in T ms) 
    WRITE_REG(TIM6_ARR, T); 
    WRITE_REG(TIM6_DIER, TIM_DIER_UIE); 
    WRITE_REG(TIM6_PSC, 0);

    WRITE_REG(TIM6_CR1, TIM_CR1_CEN);

    return 0;
}
