// code ban vet can
#include <bits/stdc++.h>
#include <time.h>

using namespace std;

int n,m,i,j,kt1,xx,yy,u,v,check,k,id,h,l1,r1,l2,r2,st,zz,uu,vv,k1,luu,turn;
char huong;
int a[11],b[11],c[11],x[11],y[11],z[11],kt[101][101];

int main(){    
    freopen("MAP.INP","r",stdin);
    scanf("%d%d%d",&n,&m,&id);
    if (n==0) exit(0);
    for (i=1;i<=n;i++){
        scanf("%d%d%d%d%d%d",&a[i],&b[i],&c[i],&x[i],&y[i],&z[i]);        
        kt[x[i]][y[i]]=1;                
    }
    fclose(stdin);

    freopen("REPORT.INP","r",stdin);
    cin>>turn;
    fclose(stdin);

    freopen("DECISION.OUT","w",stdout);
    int pos = turn;
    i = rand()%n+1;
    while(a[i]==0) i = rand()%n+1;
    if (id==1) pos += 4*8;
    pos = pos%64;
    xx = pos/8+1;
    yy = pos%8+1;
    printf("%d %d %d %d %d",1,x[i],y[i],xx,yy);
    fclose(stdout);

    return 0;
}