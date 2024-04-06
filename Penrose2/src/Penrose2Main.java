import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Penrose2Main {
	public static double phir = 2/(Math.sqrt(5)+1);
	public static double phisqr = phir*phir;
	public static int n = 4;
	public static void main(String[] args) {

		Punkt[] p = new Punkt[7];
		Kachel[] k = new Kachel[3];
		//anfangswerte für ass, in richtiger reihenfolge
		Punkt punkt1 = new Punkt(5000, 8000);
		Punkt punkt2 = new Punkt(5000, 3000);
		Punkt punkt3 = new Punkt(2061.073739, 3954.915028);
		Punkt punkt4 = new Punkt(7938.926261, 3954.915028);
		p[0] = punkt1;
		p[1] = punkt2;
		p[2] = punkt3;
		p[3] = punkt4;
		Punkt punkt5 = new Punkt((p[2].xwert-p[0].xwert)*phir+p[0].xwert, (p[2].ywert-p[0].ywert)*phir+p[0].ywert);
		Punkt punkt6 = new Punkt((p[3].xwert-p[0].xwert)*phir+p[0].xwert, (p[3].ywert-p[0].ywert)*phir+p[0].ywert);
		Punkt punkt7 = new Punkt((p[1].xwert-p[0].xwert)*phisqr+p[0].xwert, (p[1].ywert-p[0].ywert)*phisqr+p[0].ywert);

		p[4] = punkt5;
		p[5] = punkt6;
		p[6] = punkt7;
		k[0] = new Kachel(0, 1, 4, 6, 2);
		k[1] = new Kachel(0, 1, 5, 3, 6);
		k[2] = new Kachel(1, 0, 6, 4, 5);
		for (int i = 0;i<n;i++) {
			Object[] o = newGeneration(p, k);
			p = (Punkt[])o[0];
			k = (Kachel[])o[1];
		}

		System.out.println(p.length);
		System.out.println(k.length);
		
		BufferedImage bi = new BufferedImage( 10000, 10000, BufferedImage.TYPE_INT_RGB );
		{
		for (int i = 0;i<k.length;i++) {
			Graphics2D ak = bi.createGraphics();
			if (k[i].art == 0) {
				ak.setColor(Color.red);
			} else {
				ak.setColor(Color.blue);
			}
			int[] x = {(int)(p[k[i].p1].xwert), (int)(p[k[i].p3].xwert), (int)(p[k[i].p2].xwert), (int)(p[k[i].p4].xwert)};
			int[] y = {(int)(p[k[i].p1].ywert), (int)(p[k[i].p3].ywert), (int)(p[k[i].p2].ywert), (int)(p[k[i].p4].ywert)};
			ak.fillPolygon( x, y, 4);
			ak.dispose();
		}		
		}
		String typ = "png";
		try {
			if(bi == null)
			System.out.println("kein Bild vorhanden");
			else
			ImageIO.write(bi,typ,new File("bi.png"));
			}
			catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public static Object[] newGeneration(Punkt[] p, Kachel[] k) {
		Object[] result = new Object[2];
		Kachel[] kn = new Kachel[k.length*5];
		int knIndex = 0;
		Punkt[] pn = new Punkt[p.length*5];
		//p kopieren
		int pnIndex = 0;
		for (int i = 0;i<p.length;i++) {
			pn[i] = p[i];
			pnIndex++;
		}
		
		int[][] kanten = new int[p.length*5][6];//die 2 verwendeten punkte(indexe), index des inneren punktes, index des kantenpunktes, 5.: verwendet oder nicht
		int kantenIndex = 0;								//6.: ist innerer punkt bei 2 links?
		boolean kanteV = false;
		int mitteI = 0;
		
		

		for (int i = 0;i<k.length;i++) { //alle kacheln durchgehen
			if (k[i].art == 0) { //drache
				Punkt pMitte = new Punkt((p[k[i].p2].xwert-p[k[i].p1].xwert)*phir+p[k[i].p1].xwert, (p[k[i].p2].ywert-p[k[i].p1].ywert)*phir+p[k[i].p1].ywert);
				pn[pnIndex] = pMitte;
				mitteI = pnIndex;
				pnIndex++;
				//mittlerer punkt des drachen wurde erstellt
				//linke seite
				kanteV = false;
				for (int j = 0;j<kantenIndex;j++) {
					if (kanten[j][0] == k[i].p1 && kanten[j][1] == k[i].p3 || kanten[j][1] == k[i].p1 && kanten[j][0] == k[i].p3) {
						//diese kante wurde bereits verwendet, es kann ein pfeil erstellt werden
						kn[knIndex] = new Kachel(1, k[i].p1, kanten[j][3], kanten[j][2], mitteI);
						knIndex++;
						kn[knIndex] = new Kachel(0, k[i].p3, mitteI, k[i].p2, kanten[j][3]);
						knIndex++;
						kanteV = true;
						kanten[j][4] = 1;
						kanten[j][5] = 0;
						break;
					}
				}
				if (!kanteV) {
					//die linke kante wurde noch nicht verwendet
					Punkt pL = new Punkt((p[k[i].p3].xwert-p[k[i].p1].xwert)*phisqr+p[k[i].p1].xwert, (p[k[i].p3].ywert-p[k[i].p1].ywert)*phisqr+p[k[i].p1].ywert);
					pn[pnIndex] = pL;
					kanten[kantenIndex][0] = k[i].p1;
					kanten[kantenIndex][1] = k[i].p3;
					kanten[kantenIndex][2] = mitteI;
					kanten[kantenIndex][3] = pnIndex;
					kanten[kantenIndex][4] = 0;
					kanten[kantenIndex][5] = 0;
					kn[knIndex] = new Kachel(0, k[i].p3, mitteI, k[i].p2, pnIndex);
					knIndex++;
					kantenIndex++;
					pnIndex++;
					
				}
				//rechte seite
				kanteV = false;
				for (int j = 0;j<kantenIndex;j++) {
					if (kanten[j][0] == k[i].p1 && kanten[j][1] == k[i].p4 || kanten[j][1] == k[i].p1 && kanten[j][0] == k[i].p4) {
						//diese kante wurde bereits verwendet, es kann ein pfeil erstellt werden
						kn[knIndex] = new Kachel(1, k[i].p1, kanten[j][3], mitteI, kanten[j][2]);
						knIndex++;
						kn[knIndex] = new Kachel(0, k[i].p4, mitteI, kanten[j][3], k[i].p2);
						knIndex++;
						kanteV = true;
						kanten[j][4] = 1;
						kanten[j][5] = 1;
						break;
					}
				}
				if (!kanteV) {
					//die rechte kante wurde noch nicht verwendet
					Punkt pR = new Punkt((p[k[i].p4].xwert-p[k[i].p1].xwert)*phisqr+p[k[i].p1].xwert, (p[k[i].p4].ywert-p[k[i].p1].ywert)*phisqr+p[k[i].p1].ywert);
					pn[pnIndex] = pR;
					kanten[kantenIndex][0] = k[i].p1;
					kanten[kantenIndex][1] = k[i].p4;
					kanten[kantenIndex][2] = mitteI;
					kanten[kantenIndex][3] = pnIndex;
					kanten[kantenIndex][4] = 0;
					kanten[kantenIndex][5] = 1;
					kn[knIndex] = new Kachel(0, k[i].p4, mitteI, pnIndex, k[i].p2);
					knIndex++;
					kantenIndex++;
					pnIndex++;
				}
			} else { // pfeil
				kanteV = false;
				int pRI = 0;
				int pLI = 0;
				for (int j = 0;j<kantenIndex;j++) {
					if (kanten[j][0] == k[i].p1 && kanten[j][1] == k[i].p4 || kanten[j][1] == k[i].p1 && kanten[j][0] == k[i].p4) {
						//diese kante wurde bereits verwendet, es kann ein pfeil erstellt werden
						kn[knIndex] = new Kachel(1, k[i].p4, kanten[j][3], kanten[j][2], k[i].p2);
						knIndex++;
						pRI = kanten[j][3];
						kanteV = true;
						kanten[j][4] = 1;
						kanten[j][5] = 1;
						break;
					}
				}
				if (!kanteV) {
					//die rechte kante wurde noch nicht verwendet
					Punkt pR = new Punkt((p[k[i].p1].xwert-p[k[i].p4].xwert)*phisqr+p[k[i].p4].xwert, (p[k[i].p1].ywert-p[k[i].p4].ywert)*phisqr+p[k[i].p4].ywert);
					pn[pnIndex] = pR;
					pRI = pnIndex;
					kanten[kantenIndex][0] = k[i].p4;
					kanten[kantenIndex][1] = k[i].p1;
					kanten[kantenIndex][2] = k[i].p2;
					kanten[kantenIndex][3] = pnIndex;
					kanten[kantenIndex][4] = 0;
					kanten[kantenIndex][5] = 1;
					kantenIndex++;
					pnIndex++;
				}
				//linke seite
				kanteV = false;
				for (int j = 0;j<kantenIndex;j++) {
					if (kanten[j][0] == k[i].p1 && kanten[j][1] == k[i].p3 || kanten[j][1] == k[i].p1 && kanten[j][0] == k[i].p3) {
						//diese kante wurde bereits verwendet, es kann ein pfeil erstellt werden
						kn[knIndex] = new Kachel(1, k[i].p3, kanten[j][3], k[i].p2, kanten[j][2]);
						knIndex++;
						pLI = kanten[j][3];
						kanteV = true;
						kanten[j][4] = 1;
						kanten[j][5] = 0;
						break;
					}
				}
				if (!kanteV) {
					//die rechte kante wurde noch nicht verwendet
					Punkt pL = new Punkt((p[k[i].p1].xwert-p[k[i].p3].xwert)*phisqr+p[k[i].p3].xwert, (p[k[i].p1].ywert-p[k[i].p3].ywert)*phisqr+p[k[i].p3].ywert);
					pn[pnIndex] = pL;
					pLI = pnIndex;
					kanten[kantenIndex][0] = k[i].p3;
					kanten[kantenIndex][1] = k[i].p1;
					kanten[kantenIndex][2] = k[i].p2;
					kanten[kantenIndex][3] = pnIndex;
					kanten[kantenIndex][4] = 0;
					kanten[kantenIndex][5] = 0;
					kantenIndex++;
					pnIndex++;
				}
				//inneren drache erstellen
				kn[knIndex] = new Kachel(0, k[i].p1, k[i].p2, pLI, pRI);
				knIndex++;
			}
			
			
		}//i
		//übrig gebliebene kanten
		for (int i = 0;i<kantenIndex;i++) {
			if (kanten[i][4] == 0) {
				//index 0 ist spitze
				Punkt pA = new Punkt(pn[kanten[i][1]].xwert + pn[kanten[i][0]].xwert-pn[kanten[i][2]].xwert, pn[kanten[i][1]].ywert + pn[kanten[i][0]].ywert-pn[kanten[i][2]].ywert);
				pn[pnIndex] = pA;
				if (kanten[i][5] == 0) {//zuerst neu konstruierter punkt
					kn[knIndex] = new Kachel(1, kanten[i][0], kanten[i][3], pnIndex, kanten[i][2]);
				} else {
					kn[knIndex] = new Kachel(1, kanten[i][0], kanten[i][3], kanten[i][2], pnIndex);
				}
				
				pnIndex++;
				knIndex++;
			}
		}
		//arrays abschneiden, kanten kann weg
		Kachel[] kachelRes = new Kachel[knIndex];
		for (int i = 0;i<knIndex;i++) {
			kachelRes[i] = kn[i];
		}
		Punkt[] punkteRes = new Punkt[pnIndex];
		for (int i = 0;i<pnIndex;i++) {
			punkteRes[i] = pn[i];
		}
		result[0] = punkteRes;
		result[1] = kachelRes;
		
		return result;
	}
	

}
