#include <iostream>
#include <stdint.h>
#include <list>
#include <set>
#include <map>

const int M = 100000;

using namespace std;

typedef pair<int, int> bl; // ind, size


bool cmp(bl a, bl b) {
    return a.second != b.second ? a.second < b.second : a.first < b.first;
};

bool cmp_ind(bl a, bl b) {
    return a.first < b.first;
};


int main() {
    ios::sync_with_stdio(false), cin.tie(NULL), cout.tie(NULL);
    int n, m;
    cin >> n >> m;

    bl allocated[M];  // req num - block
    set<bl, decltype(cmp)*> cleared(cmp);
    set<bl, decltype(cmp_ind)*> cleared_ind(cmp_ind);
    int cur_allocated = 1;

    for (int i = 0; i < m; ++i) {
        int op; cin >> op;

        if (op > 0) { // alloc
            // check in cleared
            auto cleared_block = cleared.lower_bound(bl(0, op));

            if (cleared_block != cleared.end()) {
                cout << cleared_block->first << '\n';
                allocated[i] = bl(cleared_block->first, op);

                if (op < cleared_block->second) {
                    bl new_bl = bl(cleared_block->first + op, cleared_block->second - op);
                    cleared.insert(new_bl);
                    cleared_ind.insert(new_bl);
                }

                cleared_ind.erase(*cleared_block);
                cleared.erase(cleared_block);
            } else if (n - cur_allocated >= op) { // check memory enough
                cout << cur_allocated << '\n';
                allocated[i] = bl(cur_allocated, op);
                cur_allocated += op;
            } else {
                cout << -1 << '\n';
            }
        } else { // free
            bl free_bl = allocated[-(op + 1)];

            // if we clear last block
            if (free_bl.first + free_bl.second == cur_allocated) {
                cur_allocated -= free_bl.second;
                
                if (cleared_ind.empty()) continue;

                auto last_ind = --cleared_ind.end();
                if (last_ind->first + last_ind->second == cur_allocated) {
                    cur_allocated -= last_ind->second;
                    cleared.erase(*last_ind);
                    cleared_ind.erase(last_ind);
                }
                continue;
            }

            // else merge with cleared
            if (!cleared_ind.empty()) {
                // return block with index greater than free_bl index
                auto next_ind = cleared_ind.lower_bound(free_bl);
                auto prev_ind = next_ind != cleared_ind.begin() ? prev(next_ind) : cleared_ind.end();

                if (next_ind != cleared_ind.end() && free_bl.first + free_bl.second == next_ind->first) {
                    free_bl.second += next_ind->second;
                    cleared.erase(*next_ind);
                    cleared_ind.erase(next_ind);
                }

                if (prev_ind != cleared_ind.end() && prev_ind->first + prev_ind->second == free_bl.first) {
                    free_bl.first = prev_ind->first;
                    free_bl.second += prev_ind->second;
                    cleared.erase(*prev_ind);
                    cleared_ind.erase(prev_ind);
                }
            }

            cleared.insert(free_bl);
            cleared_ind.insert(free_bl);
        }
    }

    return 0;
}