// code cau gio de chinh thuc
#include <bits/stdc++.h>

using namespace std;

int n,m,i,j,kt1,xx,yy,u,v,check,k,doi,h,l1,r1,l2,r2,st,zz,uu,vv,k1,luu;
char huong;
int a[1001],b[1001],c[1001],x[1001],y[1001],z[1001],kt[101][101];

int main(){
    freopen("MAP.inp","r",stdin);
    scanf("%d%d%d",&n,&m,&doi);
    k1=0;
    for (i=1;i<=n;i++){
        scanf("%d%d%d%d%d%d",&a[i],&b[i],&c[i],&x[i],&y[i],&z[i]);
        if (doi==1){
            kt[x[i]][y[i]]=1;
            if (z[i]==1){
                luu=i;
                k1=1;
            }
        }
    }
    for (i=n+1;i<=n+m;i++){
        scanf("%d%d%d%d%d%d",&a[i],&b[i],&c[i],&x[i],&y[i],&z[i]);
        if (doi==2){
            kt[x[i]][y[i]]=1;
            if (z[i]==1){
                luu=i;
                k1=1;
            }
        }
    }
    fclose(stdin);
    freopen("REPORT.inp","w",stdin);
    check=0;
    kt1=0;
    while (cin >> k){
        if (k==0){
            cin >> xx >> yy >> huong;
            kt[xx][yy]=1;
        } else {
            cin >> xx >> yy >> uu >> vv >> zz;
            kt[uu][vv]=1;
        }
    }
    fclose(stdin);
    freopen("DECISION.out","w",stdout);
    for (i=0;i<50;i++){
        kt[i][0]=1;
        kt[0][i]=1;
        kt[i][51]=1;
        kt[51][i]=1;
    }
    srand((int) time(0));
        if (doi==1)
            i=rand()%n+1;
        else
            i=rand()%m+n+1;
        for (j=0;j<=10000;j++){
            h=rand()%4;
            if (h==0){
                if (kt[x[i]+1][y[i]]==0){
                    cout << "0 " << x[i] << " " << y[i] << " L"<<endl;
                    exit(0);
                }
            }
            if (h==1){
                if (kt[x[i]-1][y[i]]==0){
                    cout << "0 " << x[i] << " " << y[i] << " X"<<endl;
                    exit(0);
                }
            }
            if (h==2){
                if (kt[x[i]][y[i]+1]==0){
                    cout << "0 " << x[i] << " " << y[i] << " P"<<endl;
                    exit(0);
                }
            }
            if (h==3){
                if (kt[x[i]][y[i]-1]==0){
                    cout << "0 " << x[i] << " " << y[i] << " T"<<endl;
                    exit(0);
                }
            }
        }
}
