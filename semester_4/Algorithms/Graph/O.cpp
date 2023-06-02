#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

const int MAX_N = 100;
vector<int> graph[MAX_N];


bool check_splitable(int n) {
    vector<char> part (n, -1);
    vector<int> order (n);

    for (int cur = 0; cur < n; ++cur) {
        if (part[cur] != -1) continue;

        int h = 0, t = 0;
        part[cur] = 0;
        order[t++] = cur;

        while (h < t) {
            int v = order[h++];
            for (int i = 0; i < graph[v].size(); ++i) {
                int to = graph[v][i];

                if (part[to] == -1) {
                    part[to] = !part[v];
                    order[t++] = to;
                }
                if (part[to] == part[v]) return false;
            }
        }
    }

    return true;
}


int main() {
    int n, m; cin >> n >> m;
    
    for (int i = 0; i < m; i++){
        int from, to; cin >> from >> to;

        graph[--from].push_back(--to);
        graph[to].push_back(from);
    }

    bool is_splitable = check_splitable(n);

    cout << (is_splitable ? "YES" : "NO") << endl;
    return 0;
}