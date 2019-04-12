// code dam nhau de chinh thuc
#include <bits/stdc++.h>

using namespace std;

int kt1,k1,luu,n,m,u,v,i,j,k,xx,yy,zz,l1,l2,r1,r2,uu,vv,check,doi,h,st;
char huong;
int a[1001],b[1001],c[1001],x[1001],y[1001],z[1001],kt[100][100];

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
    freopen("REPORT.inp","r",stdin);
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
    for (i=0;i<=9;i++){
        kt[i][0]=1;
        kt[0][i]=1;
        kt[i][9]=1;
        kt[9][i]=1;
    }
    if (doi == 1){
        for (i=1;i<=n;i++)
            if (a[i]>0){
                if (kt[x[i]+1][y[i]]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " L" << endl;
                    exit(0);
                }
                if (kt[x[i]-1][y[i]]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " X" << endl;
                    exit(0);
                }
                if (kt[x[i]][y[i]+1]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " P" << endl;
                    exit(0);
                }
                if (kt[x[i]][y[i]-1]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " T" << endl;
                    exit(0);
                }
            }
    } else {
        for (i=n+1;i<=n+m;i++)
            if (a[i]>0){
                if (kt[x[i]-1][y[i]]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " X" << endl;
                    exit(0);
                }
                if (kt[x[i]+1][y[i]]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " L" << endl;
                    exit(0);
                }
                if (kt[x[i]][y[i]+1]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " P" << endl;
                    exit(0);
                }
                if (kt[x[i]][y[i]-1]==0){
                    cout << 0 << " " << x[i] << " " << y[i] << " T" << endl;
                    exit(0);
                }
            }
    }
    fclose(stdout);
}
