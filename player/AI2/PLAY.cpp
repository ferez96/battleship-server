// code cau gio 
#include <bits/stdc++.h>
#include <time.h>

using namespace std;

int n,m,i,j,kt1,xx,yy,u,v,check,k,doi,h,l1,r1,l2,r2,st,zz,uu,vv,k1,luu,turn;
char huong;
int a[11],b[11],c[11],x[11],y[11],z[11],kt[101][101];

int main(){    
    freopen("MAP.INP","r",stdin);
    scanf("%d%d%d",&n,&m,&doi);
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
    srand(turn);
    for (i=0;i<8;i++){
        kt[i][0]=1;
        kt[0][i]=1;
        kt[i][9]=1;
        kt[9][i]=1;
    }
    i=rand()%n+1;
    while(a[i]==0){
        i=rand()%n+1;
    }
    h=rand()%4;
    switch (h){
    case 0:            
        cout << "0 " << x[i] << " " << y[i] << " L"<<endl;   
        exit(0);
    case 1:            
        cout << "0 " << x[i] << " " << y[i] << " X"<<endl;
        exit(0);
    case 2:            
        cout << "0 " << x[i] << " " << y[i] << " P"<<endl;            
        exit(0);
    case 3:            
        cout << "0 " << x[i] << " " << y[i] << " T"<<endl;
        exit(0);
    }    
    fclose(stdout);

    return 0;
}
