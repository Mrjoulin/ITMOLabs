#include <iostream>
#include <algorithm>
#include <string>
#include <stack>
#include <map>

#define NUM_LETTERS 26

using namespace std;


bool weightGreater(pair<char, unsigned int> x, pair<char, unsigned int> y){
    return x.second > y.second;
}

int main() {
    string line;
    cin >> line;

    pair<char, unsigned int> alph[NUM_LETTERS];

    for (char i = 0; i < NUM_LETTERS; ++i) {
        unsigned int w;
        cin >> w;
        alph[i] = pair<char, unsigned int>('a' + i, w);
    }

    sort(alph, alph + NUM_LETTERS, weightGreater);

    map<char, int> counts;

    for (int i = 0; i < line.length(); ++i) {
        if (counts.find(line[i]) == counts.end())
            counts[line[i]] = 0;
        else 
            counts[line[i]]++;
    }

    stack<char> repeat;

    for (int i = 0; i < NUM_LETTERS; ++i) {
        char cur = alph[i].first;
        if (counts.find(cur) != counts.end() && counts[cur]) {
            cout << cur;
            repeat.push(cur);
            counts[cur] -= 2;
        }
    }

    for (auto &p : counts) {
        while (p.second >= 0) {
            cout << p.first;
            p.second--;
        }
    }

    while (!repeat.empty()) {
        cout << repeat.top();
        repeat.pop();
    }
}