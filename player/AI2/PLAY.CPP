// Strategy: random fire
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
    srand(time(0)+turn);

    for(int i=1;i<=n;++i){
        MAP[x[i]][y[i]] = -1;
    }

    for(string _cmd: cmd){
        if (_cmd.compare("-1")==0) continue;        
        int a,x,y,status;
        sscanf(_cmd.c_str(),"1 3 %d %d %d %d %d",&a,&a,&x,&y,&status);    
        MAP[x][y] = status-1;
    }
    
    while(true){
        int i = rand()%50+1;
        int j = rand()%50+1;
        for(int sid = 1;sid<=n;++sid){
            if (MAP[i][j]>=0 && abs(x[sid]-i)+abs(y[sid]-j)<=range[sid]){
                char buf[30];
                sprintf(buf,"1 3 %d %d %d %d",x[sid],y[sid],i,j);
                __output(string(buf));
                cout<<"End Program\n";
                exit(0);
            }
        }        
    }
    __output("-1");
    cout<<"Not found command\n";
    return 0;
}