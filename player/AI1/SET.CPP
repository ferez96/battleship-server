// SET.CPP
// Chien thuat: Random

#include <bits/stdc++.h>

using namespace std;

int n,m,z,id;

struct Ship{
    int hp;
    int atk;
    int range;
    int move;
};

int main(){
    // srand(7);
    freopen("SET.INP","r",stdin);
    freopen("SET.OUT","w",stdout);
    scanf("%d%d%d%d",&n,&m,&z,&id);

    vector<Ship> ships;
    vector<Ship> _ships;
    for(int i=1;i<=n;++i){
        Ship ship = Ship();
        scanf("%d%d%d%d",&ship.hp,&ship.atk,&ship.range,&ship.move);
        ships.push_back(ship);
    }
    for(int i=1;i<=m;++i){
        Ship ship = Ship();
        scanf("%d%d%d%d",&ship.hp,&ship.atk,&ship.range,&ship.move);
        _ships.push_back(ship);
        fprintf(stderr,"Enemy has ship[%2d,%d,%2d,%d]\n",ship.hp,ship.atk,ship.range,ship.move);
    }        
    
    // printf("%d\n",n);
    for (int i=1;i<=n;i++){
        int pos = 1250*(id==2)+rand()%1250;
        int x = pos/50+1;
        int y = pos%50+1;
        printf("%d %d\n",x,y);
        fprintf(stderr,"Set ship[%2d,%d,%2d,%d] at (%2d,%2d)\n",ships[i-1].hp,ships[i-1].atk,ships[i-1].range,ships[i-1].move,x,y);
    }
}
