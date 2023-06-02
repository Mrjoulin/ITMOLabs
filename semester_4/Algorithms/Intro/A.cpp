#include <iostream>

using namespace std;

int main() {
    int n;
    cin >> n;

    int arr[3];
    int cur = 0, max = 0, max_ind = 0;

    cout << (-3 % 4);

    for (int i = 0; i < n; ++i) {
        cin >> arr[i % 3];

        if (i > 1) {
            if (arr[i % 3] != arr[(i + 1) % 3] || 
                    arr[i % 3] != arr[(i + 2) % 3]) {
                if (++cur > max) max = cur, max_ind = i;
            } else cur = 1;
        } else cur = i, max = i, max_ind = i;
    }

    cout << max_ind + 1 - max << " " << max_ind + 1;
}
