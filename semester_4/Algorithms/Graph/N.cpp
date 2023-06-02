#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

const int MAX_N = 100;

vector<int> graph[MAX_N];
bool used[MAX_N];

void dfs(int v) {
	used[v] = true;

	for (int i = 0; i < graph[v].size(); ++i) {
        int cur = graph[v][i];

		if (!used[cur]) dfs(cur);
    }
}


int main() {
    int n; cin >> n;

    for (int i = 0; i < n; ++i) {
        int cur; cin >> cur; 
        graph[cur - 1].push_back(i);
        graph[i].push_back(cur - 1);

        used[i] = false;
    }

    int num_comp = 0;

	for (int i = 0; i < n; ++i) {
		if (!used[i]) {
            dfs(i);
            num_comp++;
        } 
    }

    cout << num_comp;

    return 0;
}