// code set tau dam nhau de chinh thuc

#include <bits/stdc++.h>

using namespace std;

int i,j,f,n,m,id;
int ax[21],ay[21],bx[21],by[21],x[21],y[21];

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
        for (i=4;i>=1;i--)
            for (j=1;j<=8;j++){
                printf("%d %d\n",i,j);                
                if (!n--) exit(0);
            }
    } else {
        printf("%d\n",id);
        for (i=5;i<=8;i++)
            for (j=1;j<=8;j++){
                printf("%d %d\n",i,j);                
                if (!n--) exit(0);
            }
    }
}
