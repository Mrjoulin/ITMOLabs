#include <iostream>
#include <stack>
#include <map>

using namespace std;

int main() {
    string s;
    cin >> s;

    stack<char> char_stack;
    map<char, stack<int> > index_map;
    int ans[s.length() / 2];

    int low_ind = 1;
    int big_ind = 0;

    for (char c : s) {
        bool is_low = 'a' <= c && c <= 'z';

        if (index_map.find(c) == index_map.end())
            index_map.insert(make_pair(c, stack<int>()));
        index_map[c].push(is_low ? low_ind++ : big_ind++);

        if (!char_stack.empty() && abs(char_stack.top() - c) == 32) {
            char low_char = is_low ? c : c + 32;
            char big_char = is_low ? c - 32 : c;

            ans[index_map[big_char].top()] = index_map[low_char].top();
            char_stack.pop();
            index_map[low_char].pop();
            index_map[big_char].pop();
        } else {
            char_stack.push(c);
        }
    }

    if (char_stack.empty()) {
        cout << "Possible\n";
        for (int i : ans) cout << i << " ";
    } else cout << "Impossible";
}
