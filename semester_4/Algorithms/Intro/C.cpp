#include <iostream>
#include <string>
#include <stack>
#include <vector>
#include <set>
#include <map>

using namespace std;

int main() {
    string line;

    map<string, stack<int> > mem;
    stack<set<string> > changed;
    changed.push(set<string>());

    while (cin >> line) {
        if (line[0] == '{') { // block process
            changed.push(set<string>());
        } else if (line[0] == '}') {
            for (auto const& var : changed.top() ) {
                mem[var].pop();
                if (mem[var].empty()) mem.erase(var);
            }
            changed.pop();
        } else { // Var process
            string var1 = ""; 
            string var2s = ""; int var2i = 0;

            int sep = 0;
            for (; line[sep] != '='; ++sep) var1 += line[sep];

            sep++;
            bool is_num = line[sep] >= '0' && line[sep] <= '9' || line[sep] == '-';

            for (int i = sep; line[i]; ++i) {
                if (is_num) 
                    var2i = line[i] == '-' ? var2i : (var2i * 10 + (line[i] - '0'));
                else 
                    var2s += line[i];
            }
            if (line[sep] == '-') var2i = -var2i;

            size_t var1_exist = mem.count(var1);
            bool was_in_changed = !(changed.top().insert(var1).second);

            if (is_num) { // var1 = number
                if (var1_exist) {
                    if (was_in_changed) mem[var1].top() = var2i;
                    else mem[var1].push(var2i);
                } else {
                    mem[var1] = stack<int>();
                    mem[var1].push(var2i);
                }
            } else { // var1 = var2
                size_t var2_exist = mem.count(var2s);

                if (var1_exist) {
                    if (var2_exist) {
                        if (was_in_changed) mem[var1].top() = mem[var2s].top();
                        else mem[var1].push(mem[var2s].top());
                    }
                    else {
                        if (was_in_changed) mem[var1].top() = 0;
                        else mem[var1].push(0);
                    }
                } else {
                    mem[var1] = stack<int>();

                    if (var2_exist) mem[var1].push(mem[var2s].top());
                    else mem[var1].push(0);
                }
                cout << mem[var1].top() << endl;
            }
        }
    }

    return 0;
}