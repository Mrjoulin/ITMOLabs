#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <stddef.h>
#include <inttypes.h>
#include <math.h>


int main() {
    int a, b, c, d;
    scanf("%d %d %d %d", &a, &b, &c, &d);
    uint64_t k;
    scanf("%" SCNu64, &k);
    int64_t ans = a;

    if (b == 1) {
        double n = (double) a / c;
        ans = k > n ? 0 : a - k * c;
    } else if (a * b - c < a) {
        double n = log( (double) c / (c - a * (b - 1)) ) / log(b);

        if (k < n) {
            ans = a + (a * (b - 1) - c) * ((int64_t) ((pow(b, k) - 1) / (b - 1)));
        } else ans = 0;
    } else if (a * b - c > a){
        double n = log( (double) (d * (b - 1) - c) / (a * (b - 1) - c) ) / log(b);

        if (k < n) {
            ans = a + (a * (b - 1) - c) * ((int64_t) ((pow(b, k) - 1) / (b - 1)));
        } else ans = d;
    }

    printf("%" PRId64, ans);
}