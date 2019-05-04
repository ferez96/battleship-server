// Strategy: Random runing
#include <bits/stdc++.h>

using namespace std;

const char Direction[] = {'U','D','L','R'};
const int dx[] = {-1,+1,0,0};
const int dy[] = {0,0,-1,+1};
const int SEED = 7;

int n,z,turn;
int hp[20],atk[20],range[20],_move[20];
int x[20],y[20];
int MAP[51][51];
int status[20];
string cmd[501];

void __input(){
    ifstream fi;
    fi.open("MAP.INP",ios::in);
    fi>>n>>z;
    if (n==0) exit(0); // have no ship left
    for (int i=1;i<=n;++i){
        fi>>hp[i]>>atk[i]>>range[i]>>_move[i]>>x[i]>>y[i]>>status[i];
    }
    for(int i=1;i<=z;++i){
        int x,y;
        fi>>x>>y;
        MAP[x][y] = -1;
    }
    fi.close();

    fi.open("REPORT.INP",ios::in);
    fi>>turn;
    for(int i=1;i<=turn;++i){
        getline(fi,cmd[i]);
    }
    fi.close();
}

void __output(string cmd){
    ofstream fo;
    fo.open("DECISION.OUT",ios::out|ios::trunc);
    fo<<cmd;
    fo.close();
}

int main(){
    cout<<"Start program\n";
    __input();

    for(int i=1;i<=n;++i){
        MAP[x[i]][y[i]] = 1;
    }
    srand(SEED+turn);
    std::ostringstream stringStream;
    stringStream << "0 ";
    int ship_id = rand()%n + 1;
    stringStream << x[ship_id] << " " << y[ship_id] << " ";
    int n_move = rand()%_move[ship_id] + 1;
    for(int i=1;i<=n_move;++i) stringStream<<Direction[rand()%4];
    __output(stringStream.str());
    cout<<"End Program\n";
    return 0;
}