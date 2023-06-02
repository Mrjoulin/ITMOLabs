#include <iostream>
#include <vector>
#include <queue>
#include <map>
#include <set>

const int N = 100000;
const int P = 500000;

using namespace std;

int main() {
    int n, k, p;
    cin >> n >> k >> p;

    int req[P];
    queue<int> pos[N];

    for (int i = 0; i < p; ++i) {
        int cur; cin >> cur;
        req[i] = cur - 1;
        pos[cur - 1].push(i);
    }

    set<int> on_floor;
    set<int> next_ind;
    queue<int> next_remove;
    int num_descents = 0;

    for (int i = 0; i < p; ++i) {
        if (!next_ind.empty() && *next_ind.begin() == i) {
            next_ind.erase(next_ind.begin());
        } else {
            ++num_descents;

            if (on_floor.size() == k) {  
                if (next_remove.empty()) {
                    auto to_remove = --next_ind.end();
                    on_floor.erase(req[*to_remove]); // log
                    next_ind.erase(to_remove);
                } else {
                    int to_remove = next_remove.front();
                    on_floor.erase(to_remove);       // log
                    next_remove.pop();
                }
            }

            on_floor.insert(req[i]); // log
        }

        pos[req[i]].pop();

        if (!pos[req[i]].empty()) {
            next_ind.insert(pos[req[i]].front());
        } else {
            next_remove.push(req[i]);
        }
    }

    cout << num_descents;

    return 0;
}