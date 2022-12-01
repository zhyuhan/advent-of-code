#include <bits/stdc++.h>

using namespace std;

int main() {
    ifstream input("./input.in");

    priority_queue<int, vector<int>, greater<int>> cals;

    int curr = 0;

    string line;
    while (getline(input, line)) {
        if (line == "") {
            if (cals.size() < 3) {
                cals.push(curr);
            } else {
                if (curr > cals.top()) {
                    cals.pop();
                    cals.push(curr);
                }
            }
            curr = 0;
        } else {
            curr += stoi(line);
        }
    }

    int first = cals.top();
    cals.pop();
    int second = cals.top();
    cals.pop();
    int third = cals.top();
    cals.pop();

    // part 1
    cout << third << endl;

    // part 2
    cout << first + second + third << endl;
}
