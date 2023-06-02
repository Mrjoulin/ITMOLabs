#include <iostream>

using namespace std;


int main() {
    int n, k;
    cin >> n >> k;
    
    int s[n];
    for (int i = 0; i < n; ++i) cin >> s[i];

    int l = 1, r = s[n - 1] - s[0];
    
    while (k > 2 && l != r) {
        int cur = (l + r) / 2;
        int cnt = 1;
 
        for (int prev = 0, i = 1; i < n && cnt < k; ++i) {
            if (s[i] - s[prev] > cur) {
                prev = i;
                cnt++;
            }
        }
 
        if (cnt == k) l = cur + 1;
        else r = cur;
    }

    cout << r;

    return 0;
}