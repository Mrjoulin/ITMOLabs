#include <iostream>
#include <algorithm>

using namespace std;

int main() {
    int n, k;
    cin >> n >> k;

    int prices[n];
    for (int i = 0; i < n; ++i) cin >> prices[i];

    sort(prices, prices + n);

    int s = 0;
    for (int i = 0; i < n; ++i) {
        if ( i % k != n % k ) {
            s += prices[i];
        }
    }

    cout << s;

    return 0;
}