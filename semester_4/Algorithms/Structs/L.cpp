#include <iostream>
#include <queue>
#include <set>

using namespace std;


int main() {
    int n, k;
    cin >> n >> k;

    queue<int> prev;
    multiset<int> window;

    for (int i = 0; i < n; ++i) {
        int cur;
        cin >> cur;

        prev.push(cur);
        window.insert(cur);

        if (i >= k - 1) {
            cout << *window.begin() << ' ';

            window.erase(window.lower_bound(prev.front()));
            prev.pop();
        }
    }
}