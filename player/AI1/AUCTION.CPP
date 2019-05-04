// Strategy: Random

#include <bits/stdc++.h>
using namespace std;

int main(){
	// srand(7);
	freopen("AUCTION.OUT","w",stdout);
	int sum = 0;
	for(int i=1;i<=5;++i) {
		int p = rand()%20;
		sum += p;
		cout<<p<<" ";
	}
	int p = (100-sum)/5;
	for(int i=6;i<=10;++i) cout<<p<<" ";
	return 0;
}