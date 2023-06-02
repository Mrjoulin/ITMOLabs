#include <iostream>
#include <list>

using namespace std;


int main() {
    ios::sync_with_stdio(false), cin.tie(NULL), cout.tie(NULL);    
    int n; cin >> n;

    list<int> order;
    auto mid = order.begin();
    char op;

    while (cin >> op) {
        if (op == '+' || op == '*') {
            int num;
            cin >> num;
    
            if (op == '+')
                order.push_back(num);
            else 
                order.insert(order.size() > 1 ? next(mid) : order.end(), num);

            mid = next(mid, order.size() % 2);
        } else {
            if (order.size() > 1) mid = next(mid, 1 - order.size() % 2);

            cout << order.front() << "\n";
            order.pop_front();

            if (order.empty()) mid = order.begin();
        }
    }

    return 0;
}