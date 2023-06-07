#include "hal.h"

const int DELAY = 500;
const char NUM_FRAMES = 8;
const char NUM_LEDS = 8;
const char NUM_SW = 4;
const char CONTROL_N = 0x7;

unsigned int LEDS_PINS[NUM_LEDS] = {
    GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5,
    GPIO_PIN_6, GPIO_PIN_8, GPIO_PIN_9,
    GPIO_PIN_11, GPIO_PIN_12
};
unsigned int SW_PINS[NUM_SW] = {GPIO_PIN_4, GPIO_PIN_8, GPIO_PIN_10, GPIO_PIN_12};
unsigned int BTN_PIN = GPIO_PIN_15;
enum Color { Green = GPIO_PIN_13, Yelow = GPIO_PIN_14, Red = GPIO_PIN_15 };


void set_color(Color color) {
    HAL_GPIO_WritePin(GPIOD, Green, color == Green ? GPIO_PIN_SET : GPIO_PIN_RESET);
    HAL_GPIO_WritePin(GPIOD, Yelow, color == Yelow ? GPIO_PIN_SET : GPIO_PIN_RESET);
    HAL_GPIO_WritePin(GPIOD, Red, color == Red ? GPIO_PIN_SET : GPIO_PIN_RESET);
}

void btn_process() {
    GPIO_PinState state = HAL_GPIO_ReadPin(GPIOC, BTN_PIN);

    if (state == GPIO_PIN_RESET) {
        set_color(Red);

        // wait button release
        while (state == GPIO_PIN_RESET) state = HAL_GPIO_ReadPin(GPIOC, BTN_PIN);
        // wait next click
        while(state == GPIO_PIN_SET) state = HAL_GPIO_ReadPin(GPIOC, BTN_PIN);
        // wait button release
        while (state == GPIO_PIN_RESET) state = HAL_GPIO_ReadPin(GPIOC, BTN_PIN);
    }
}


GPIO_PinState animation_led_check(int frame, int cur_led) {
    int mid_frames = (NUM_FRAMES - 1) / 2, mid_leds = (NUM_LEDS - 1) / 2;
    int frame_diff = frame > mid_frames ? frame - mid_frames : mid_frames - frame;

    bool condition = cur_led <= mid_leds - frame_diff || cur_led >= frame_diff + mid_leds + 1;

    return condition ? GPIO_PIN_SET : GPIO_PIN_RESET;
}

int umain() {
    int cur_frame = 0;

    while(true) {
        char cur_n = 0;
        
        for(int i = 0; i < NUM_SW; ++i) {
            GPIO_PinState state = HAL_GPIO_ReadPin(GPIOE, SW_PINS[i]);
            cur_n += state << i;
        }

        if (cur_n == CONTROL_N) {
            btn_process();
            set_color(Green);

            // animation
            for (int i = 0; i < NUM_LEDS; ++i) {    
                HAL_GPIO_WritePin(GPIOD, LEDS_PINS[i], animation_led_check(cur_frame, i));
            }
            HAL_Delay(DELAY);
            
            cur_frame = (cur_frame + 1) % NUM_FRAMES;
        } else {
            set_color(Yelow);

            for (int i = 0; i < NUM_LEDS; ++i, cur_n /= 2) {
                HAL_GPIO_WritePin(GPIOD, LEDS_PINS[i], cur_n % 2 ? GPIO_PIN_SET : GPIO_PIN_RESET);
            }

            cur_frame = 0;
        }
    }

    return 0;
}