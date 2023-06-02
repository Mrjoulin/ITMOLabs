#include <iostream>
#include <stdlib.h>
#include <vector>
#include <set>

using namespace std;


struct digits {
    int counts[11]; // if 10th element - end of digit
    digits* next[10];
};


int find_index(digits* d) {
    for (int i = 9; i >= 0; i--)
        if (d->counts[i]) return i;
    return -1;
}

void print_number(digits* d, vector<int> prev) {
    int ind = find_index(d);

    if (ind >= 0 && d->counts[10]) {
        digits* cur = d;
        int next_ind = ind;
        set<int> stops_ind;
        bool lower = false, greater = false;

        while (!lower && !greater && next_ind >= 0) {
            for (int prev_ind = 0; prev_ind < prev.size(); prev_ind++) {
                lower = next_ind < prev[prev_ind];
                greater = next_ind > prev[prev_ind];

                if (lower || greater) break;

                cur = cur->next[next_ind];

                if (cur->counts[10] && prev_ind + 1 < prev.size()) 
                    stops_ind.insert(prev_ind + 1);

                next_ind = find_index(cur);
                if (next_ind < 0) break;
            }
        }

        if (lower && stops_ind.empty()) {
            d->counts[10]--;
            return;
        }

        int prev_start_ind = 0;
        while (!greater && !stops_ind.empty() && prev_start_ind < prev.size()) {
            for (auto it = stops_ind.begin(); it != stops_ind.end(); ) {
                int prev_ind = *it;
                int compare_ind = prev_ind + prev_start_ind < prev.size() ? prev_ind + prev_start_ind : prev.size() - 1;
                int diff = prev[prev_start_ind] - prev[compare_ind];

                if (diff) {
                    greater = diff > 0;
                    if (greater) break;

                    it = stops_ind.erase(it);
                } else ++it;
            }
            prev_start_ind++;
        }

        if (!greater) {
            d->counts[10]--;
            return;
        }
    } else if (ind < 0) {
        if (d->counts[10]) d->counts[10]--;
        return;
    }

    cout << ind;
    d->counts[ind]--;
    prev.push_back(ind);

    print_number(d->next[ind], prev);
}


int main() {
    digits* d = (digits*) malloc(sizeof(digits));
    int num = 0;
    
    string line;

    while (cin >> line) {
        num++;

        digits* cur = d;

        for (char c : line) {
            char ind = c - '0';

            cur->counts[ind]++;

            if (!cur->next[ind]) {
                digits* next_d = (digits*) malloc(sizeof(digits));
                cur->next[ind] = next_d;
                cur = next_d;
            } else {
                cur = cur->next[ind];
            }
        }
        cur->counts[10]++;
    }

    while (num--) { 
        print_number(d, vector<int>());
    }

    return 0;
}