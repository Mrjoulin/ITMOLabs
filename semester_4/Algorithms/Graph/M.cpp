#include <iostream>
#include <algorithm>
#include <list>
#include <queue>
#include <set>

using namespace std;

typedef pair<int, int> coord;
typedef pair<int, coord> weighted_coord;

const int BIG_CONST = 1000000;
const int MAX_SIZE = 1000;
char player_map[MAX_SIZE + 2][MAX_SIZE + 2] = {0};
int weight[MAX_SIZE + 2][MAX_SIZE + 2] = {0};
char path[MAX_SIZE + 2][MAX_SIZE + 2] = {0};


char get_direction(coord cur, coord near) {
  coord diff = coord(near.first - cur.first, near.second - cur.second);

  if (diff == coord(0, 1)) return 'E';
  if (diff == coord(1, 0)) return 'S';
  if (diff == coord(0, -1)) return 'W';
  if (diff == coord(-1, 0)) return 'N';

  return 0;
}


list<char> search(coord start, coord end) {
  set<weighted_coord> dist;

  weight[start.first][start.second] = 0;
  dist.insert(weighted_coord(weight[start.first][start.second], start));

  while (!dist.empty()) {
    coord cur = dist.begin()->second;
    dist.erase(dist.begin());

    if (cur == end) continue;

    for (int diff = 1; diff > -2; diff -= 2) {
      for (int cur_ax = 0; cur_ax < 2; ++cur_ax) {
        coord near = cur_ax ? coord(cur.first, cur.second + diff) : coord(cur.first + diff, cur.second);

        if (weight[cur.first][cur.second] + player_map[near.first][near.second] < weight[near.first][near.second]) {
          dist.erase(weighted_coord(weight[near.first][near.second], near));

          weight[near.first][near.second] = weight[cur.first][cur.second] + player_map[near.first][near.second];
          path[near.first][near.second] = get_direction(cur, near);

          dist.insert(weighted_coord(weight[near.first][near.second], near));
        }
      }
    }
  }

  list<char> res_path;
  int y = end.first, x = end.second;

  while (path[y][x]) {
    res_path.push_front(path[y][x]);

    switch (path[y][x]){
      case 'E':
        x -= 1; break;
      case 'S':
        y -= 1; break;
      case 'W':
        x += 1; break;
      case 'N':
        y += 1; break;
    }
  }

  return res_path;
}


int main() {
  int N, M, xStart, yStart, xEnd, yEnd;
  cin >> N >> M >> xStart >> yStart >> xEnd >> yEnd;

  for (int i = 1; i <= N; ++i) {
    for (int j = 1; j <= M; ++j) {
      char cur; cin >> cur;
      if (cur == '.' || cur == 'W') {
        player_map[i][j] = cur == '.' ? 1 : 2;
        weight[i][j] = BIG_CONST;
      }
    }
  }

  list<char> res_path = search(coord(xStart, yStart), coord(xEnd, yEnd));

  if (!res_path.empty()) {
    cout << weight[xEnd][yEnd] << endl;
    for (char const& c : res_path) cout << c;
  } else cout << -1;

  return 0;
}