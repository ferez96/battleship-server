// code set tau cau gio de chinh thuc

#include <bits/stdc++.h>

using namespace std;

int i,j,n,m,id,l,r;
int ax[21],ay[21],bx[21],by[21],kt[100][100];

int main(){
    freopen("SET.INP","r",stdin);
    freopen("SET.OUT","w",stdout);
    scanf("%d%d%d",&n,&m,&id);
    for (i=1;i<=n;i++)
        scanf("%d",&ax[i],&ay[i]);
    for (i=1;i<=m;i++)
        scanf("%d",&bx[i],&by[i]);
    if (id==1){
        printf("%d\n",id);
        srand((int) time(0));
        for (i=1;i<=10000;i++){
            l=rand()%4+1;
            r=rand()%8+1;
            if (kt[l][r]==0){
                kt[l][r]=1;
                printf("%d %d\n",l,r);
                n--;
                if (n==0) exit(0);
            }
        }
    } else {
        printf("%d\n",id);
        srand((int) time(0));
        for (i=1;i<=10000;i++){
            l=rand()%4+26;
            r=rand()%8+1;
            if (kt[l][r]==0){
                kt[l][r]=1;
                printf("%d %d\n",l,r);
                m--;
                if (m==0) exit(0);
            }
        }
    }
}
