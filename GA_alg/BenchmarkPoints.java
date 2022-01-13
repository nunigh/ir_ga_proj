
import java.util.ArrayList;

import org.opencv.core.Point;


public class BenchmarkPoints {

	
	public static ArrayList<ArrayList<Point>> get_Sample()
	{
		ArrayList<Point> real1 = new ArrayList<Point>();
		ArrayList<Point> real2 = new ArrayList<Point>();		

		ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
		arr.add(real1);
		arr.add(real2);
		return arr;
	}
	
	public static ArrayList<ArrayList<Point>> get_PointSet1()
	{
		ArrayList<Point> real1 = new ArrayList<Point>();
		ArrayList<Point> real2 = new ArrayList<Point>();		

		real1.add(new Point(0,0.5087275324641088));
		real1.add(new Point(0.0169491525423729,0.5609439537619204));
		real1.add(new Point(0.0338983050847458,0.6124926603489741));
		real1.add(new Point(0.0508474576271187,0.6628088738086418));
		real1.add(new Point(0.06779661016949151,0.7113413191769149));
		real1.add(new Point(0.08474576271186443,0.7575582648262715));
		real1.add(new Point(0.1016949152542373,0.8009533482286595));
		real1.add(new Point(0.1186440677966102,0.8410511237693173));
		real1.add(new Point(0.1355932203389831,0.8774122718284227));
		real1.add(new Point(0.1525423728813559,0.9096384120587805));
		real1.add(new Point(0.1694915254237289,0.9373764681242737));
		real1.add(new Point(0.1864406779661018,0.960322536078087));
		real1.add(new Point(0.2033898305084747,0.9782252139979454));
		real1.add(new Point(0.2203389830508474,0.9908883563981825));
		real1.add(new Point(0.2372881355932203,0.9981732232407283));
		real1.add(new Point(0.2542372881355933,1));
		real1.add(new Point(0.2711864406779662,0.9963486721275449));
		real1.add(new Point(0.288135593220339,0.987259244335623));
		real1.add(new Point(0.3050847457627119,0.9728313022972082));
		real1.add(new Point(0.3220338983050848,0.9532229215645137));
		real1.add(new Point(0.3389830508474577,0.9286489356601649));
		real1.add(new Point(0.3559322033898306,0.8993785823161666));
		real1.add(new Point(0.3728813559322034,0.8657325536489713));
		real1.add(new Point(0.3898305084745763,0.8280794825895406));
		real1.add(new Point(0.4067796610169492,0.7868319040638133));
		real1.add(new Point(0.4237288135593221,0.7424417351737285));
		real1.add(new Point(0.440677966101695,0.6953953238988851));
		real1.add(new Point(0.4576271186440678,0.6462081205663056));
		real1.add(new Point(0.4745762711864407,0.5954190304687853));
		real1.add(new Point(0.4915254237288136,0.543584509505733));
		real1.add(new Point(0.5084745762711864,0.4912724675358914));
		real1.add(new Point(0.5254237288135594,0.4390560462380796));
		real1.add(new Point(0.5423728813559322,0.3875073396510259));
		real1.add(new Point(0.5593220338983051,0.3371911261913581));
		real1.add(new Point(0.576271186440678,0.288658680823085));
		real1.add(new Point(0.5932203389830508,0.2424417351737285));
		real1.add(new Point(0.6101694915254237,0.1990466517713406));
		real1.add(new Point(0.6271186440677966,0.1589488762306826));
		real1.add(new Point(0.6440677966101694,0.1225877281715774));
		real1.add(new Point(0.6610169491525423,0.09036158794121953));
		real1.add(new Point(0.6779661016949152,0.06262353187572663));
		real1.add(new Point(0.6949152542372882,0.03967746392191308));
		real1.add(new Point(0.711864406779661,0.02177478600205463));
		real1.add(new Point(0.728813559322034,0.009111643601817519));
		real1.add(new Point(0.7457627118644068,0.001826776759271616));
		real1.add(new Point(0.7627118644067796,0));
		real1.add(new Point(0.7796610169491526,0.003651327872455335));
		real1.add(new Point(0.7966101694915255,0.0127407556643771));
		real1.add(new Point(0.8135593220338982,0.02716869770279191));
		real1.add(new Point(0.8305084745762711,0.04677707843548617));
		real1.add(new Point(0.8474576271186441,0.07135106433983505));
		real1.add(new Point(0.8644067796610169,0.1006214176838332));
		real1.add(new Point(0.8813559322033899,0.1342674463510289));
		real1.add(new Point(0.8983050847457628,0.1719205174104593));
		real1.add(new Point(0.9152542372881356,0.2131680959361863));
		real1.add(new Point(0.9322033898305083,0.2575582648262713));
		real1.add(new Point(0.9491525423728814,0.3046046761011147));
		real1.add(new Point(0.9661016949152542,0.3537918794336942));
		real1.add(new Point(0.9830508474576272,0.4045809695312148));
		real1.add(new Point(1,0.4564154904942668));
	
		real2.add(new Point(1.262855156573388,1.492369461205559));
		real2.add(new Point(1.350370856185729,1.552036942646946));
		real2.add(new Point(1.437025733286021,1.611034410085725));
		real2.add(new Point(1.522091671558241,1.668795140277048));
		real2.add(new Point(1.60485796343267,1.724765959928156));
		real2.add(new Point(1.684639096746759,1.778413306380731));
		real2.add(new Point(1.760782265359826,1.829229073435036));
		real2.add(new Point(1.83267452143375,1.876736178267768));
		real2.add(new Point(1.899749491017749,1.920493788451306));
		real2.add(new Point(1.961493579359906,1.960102151806036));
		real2.add(new Point(2.017451597958747,1.995206976168888));
		real2.add(new Point(2.067231751703721,2.025503311092412));
		real2.add(new Point(2.110509931464415,2.050738888945698));
		real2.add(new Point(2.147033265098016,2.070716888811326));
		real2.add(new Point(2.176622887969421,2.085298092896532));
		real2.add(new Point(2.199175902629607,2.094402411832469));
		real2.add(new Point(2.214666506181622,2.098009762150102));
		real2.add(new Point(2.223146272982517,2.096160286318891));
		real2.add(new Point(2.224743589583904,2.088953912937499));
		real2.add(new Point(2.219662248102019,2.076549261895145));
		real2.add(new Point(2.208179213428664,2.05916190649892));
		real2.add(new Point(2.190641588745884,2.037062011607507));
		real2.add(new Point(2.167462812590874,2.010571373648465));
		real2.add(new Point(2.139118129136843,1.980059894949222));
		real2.add(new Point(2.106139381318406,1.945941531009771));
		real2.add(new Point(2.069109183849091,1.908669755119585));
		real2.add(new Point(2.028654539972626,1.868732590009365));
		real2.add(new Point(1.985439971884184,1.826647260971873));
		real2.add(new Point(1.940160240086158,1.782954529033394));
		real2.add(new Point(1.893532731446704,1.738212766262762));
		real2.add(new Point(1.846289599359096,1.692991838130136));
		real2.add(new Point(1.79916974211593,1.64786685994165));
		real2.add(new Point(1.752910707384812,1.603411895755775));
		real2.add(new Point(1.708240611481768,1.560193668817355));
		real2.add(new Point(1.665870161976513,1.51876535241915));
		real2.add(new Point(1.626484871031599,1.479660509219478));
		real2.add(new Point(1.590737544787707,1.443387245418076));
		real2.add(new Point(1.559241131082957,1.410422643838246));
		real2.add(new Point(1.532562003868133,1.381207536907612));
		real2.add(new Point(1.511213757895151,1.356141676805784));
		real2.add(new Point(1.495651581665485,1.335579355695836));
		real2.add(new Point(1.486267270289686,1.319825524025214));
		real2.add(new Point(1.483384932898166,1.309132449424832));
		real2.add(new Point(1.487257441633739,1.303696952812106));
		real2.add(new Point(1.49806366113151,1.303658251979804));
		real2.add(new Point(1.515906488840498,1.309096436296769));
		real2.add(new Point(1.540811727657658,1.320031589232038));
		real2.add(new Point(1.572727803225937,1.336423568316152));
		real2.add(new Point(1.611526328993726,1.358172444950449));
		real2.add(new Point(1.657003512844786,1.385119599245705));
		real2.add(new Point(1.708882389887316,1.417049457894833));
		real2.add(new Point(1.766815856939269,1.453691856039148));
		real2.add(new Point(1.830390475463455,1.494724997251093));
		real2.add(new Point(1.89913100128666,1.539778979203239));
		real2.add(new Point(1.972505591474273,1.588439846395593));
		real2.add(new Point(2.049931631312761,1.640254125538682));
		real2.add(new Point(2.130782117558402,1.694733793901805));
		real2.add(new Point(2.214392528016018,1.7513616261922));
		real2.add(new Point(2.300068102183219,1.809596861383583));
		real2.add(new Point(2.387091453191847,1.868881127407117));

		ArrayList<Point> real1t = new ArrayList<Point>();
		ArrayList<Point> real2t = new ArrayList<Point>();		

		int factor = 250;
		for (int i = 0; i < real1.size(); i++) {
			real1t.add(new Point (real1.get(i).x * factor, real1.get(i).y * factor));
		}
		for (int i = 0; i < real2.size(); i++) {
			real2t.add(new Point (real2.get(i).x * factor, real2.get(i).y * factor));
		}
		
		ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
		arr.add(real1t);
		arr.add(real2t);
		return arr;
	}
	
	public static ArrayList<ArrayList<Point>> get_PointSet2()
	{
		ArrayList<Point> real1 = new ArrayList<Point>();
		ArrayList<Point> real2 = new ArrayList<Point>();		

		real1.add(new Point(	0.378167641	,	0.096296296	)	)	;
		real1.add(new Point(	0.339181287	,	0.111111111	)	)	;
		real1.add(new Point(	0.304093567	,	0.148148148	)	)	;
		real1.add(new Point(	0.292397661	,	0.185185185	)	)	;
		real1.add(new Point(	0.280701754	,	0.222222222	)	)	;
		real1.add(new Point(	0.28460039	,	0.255555556	)	)	;
		real1.add(new Point(	0.272904483	,	0.274074074	)	)	;
		real1.add(new Point(	0.233918129	,	0.251851852	)	)	;
		real1.add(new Point(	0.194931774	,	0.274074074	)	)	;
		real1.add(new Point(	0.167641326	,	0.311111111	)	)	;
		real1.add(new Point(	0.155945419	,	0.348148148	)	)	;
		real1.add(new Point(	0.152046784	,	0.385185185	)	)	;
		real1.add(new Point(	0.155945419	,	0.422222222	)	)	;
		real1.add(new Point(	0.159844055	,	0.459259259	)	)	;
		real1.add(new Point(	0.171539961	,	0.492592593	)	)	;
		real1.add(new Point(	0.183235867	,	0.525925926	)	)	;
		real1.add(new Point(	0.191033138	,	0.562962963	)	)	;
		real1.add(new Point(	0.202729045	,	0.592592593	)	)	;
		real1.add(new Point(	0.222222222	,	0.618518519	)	)	;
		real1.add(new Point(	0.237816764	,	0.640740741	)	)	;
		real1.add(new Point(	0.257309942	,	0.659259259	)	)	;
		real1.add(new Point(	0.292397661	,	0.666666667	)	)	;
		real1.add(new Point(	0.331384016	,	0.662962963	)	)	;
		real1.add(new Point(	0.37037037	,	0.655555556	)	)	;
		real1.add(new Point(	0.389863548	,	0.640740741	)	)	;
		real1.add(new Point(	0.409356725	,	0.618518519	)	)	;
		real1.add(new Point(	0.428849903	,	0.603703704	)	)	;
		real1.add(new Point(	0.44834308	,	0.581481481	)	)	;
		real1.add(new Point(	0.467836257	,	0.559259259	)	)	;
		real1.add(new Point(	0.487329435	,	0.537037037	)	)	;
		real1.add(new Point(	0.413255361	,	0.103703704	)	)	;
		real1.add(new Point(	0.44834308	,	0.111111111	)	)	;
		real1.add(new Point(	0.483430799	,	0.118518519	)	)	;
		real1.add(new Point(	0.514619883	,	0.12962963	)	)	;
		real1.add(new Point(	0.549707602	,	0.137037037	)	)	;
		real1.add(new Point(	0.576998051	,	0.148148148	)	)	;
		real1.add(new Point(	0.604288499	,	0.162962963	)	)	;
		real1.add(new Point(	0.623781676	,	0.181481481	)	)	;
		real1.add(new Point(	0.643274854	,	0.2	)	)	;
		real1.add(new Point(	0.658869396	,	0.233333333	)	)	;
		real1.add(new Point(	0.666666667	,	0.27037037	)	)	;
		real1.add(new Point(	0.651072125	,	0.307407407	)	)	;
		real1.add(new Point(	0.639376218	,	0.344444444	)	)	;
		real1.add(new Point(	0.608187135	,	0.374074074	)	)	;
		real1.add(new Point(	0.596491228	,	0.411111111	)	)	;
		real1.add(new Point(	0.561403509	,	0.433333333	)	)	;
		real1.add(new Point(	0.53411306	,	0.47037037  )   )	;
		real1.add(new Point(	0.510721248	,	0.507407407	)	)	;
		real1.add(new Point(	0.335282651	,	0.118518519	)	)	;
		real1.add(new Point(	0.28460039	,	0.214814815	)	)	;

		
		real2.add(new Point(	0.453122069	,	1.067347364	)	)	;
		real2.add(new Point(	0.451839592	,	1.09180919	)	)	;
		real2.add(new Point(	0.477799343	,	1.161881905	)	)	;
		real2.add(new Point(	0.508806564	,	1.223828751	)	)	;
		real2.add(new Point(	0.530700655	,	1.277384737	)	)	;
		real2.add(new Point(	0.556835484	,	1.330635914	)	)	;
		real2.add(new Point(	0.561429257	,	1.351747453	)	)	;
		real2.add(new Point(	0.528239909	,	1.301176137	)	)	;
		real2.add(new Point(	0.518037318	,	1.298368514	)	)	;
		real2.add(new Point(	0.502141358	,	1.300402848	)	)	;
		real2.add(new Point(	0.490796142	,	1.303064032	)	)	;
		real2.add(new Point(	0.482739893	,	1.316195866	)	)	;
		real2.add(new Point(	0.478068541	,	1.333475219	)	)	;
		real2.add(new Point(	0.470070858	,	1.341325698	)	)	;
		real2.add(new Point(	0.4715903	,	1.361339284	)	)	;
		real2.add(new Point(	0.477413265	,	1.38931509	)	)	;
		real2.add(new Point(	0.483385603	,	1.429436779	)	)	;
		real2.add(new Point(	0.49867332	,	1.474730336	)	)	;
		real2.add(new Point(	0.52794035	,	1.535192694	)	)	;
		real2.add(new Point(	0.550986266	,	1.577085086	)	)	;
		real2.add(new Point(	0.571770106	,	1.60881765	)	)	;
		real2.add(new Point(	0.598186758	,	1.633656511	)	)	;
		real2.add(new Point(	0.622316357	,	1.647529109	)	)	;
		real2.add(new Point(	0.650441417	,	1.66288334	)	)	;
		real2.add(new Point(	0.658663196	,	1.65581515	)	)	;
		real2.add(new Point(	0.665243008	,	1.639201318	)	)	;
		real2.add(new Point(	0.669870257	,	1.626989712	)	)	;
		real2.add(new Point(	0.676111061	,	1.610152756	)	)	;
		real2.add(new Point(	0.684309112	,	1.596701439	)	)	;
		real2.add(new Point(	0.705772749	,	1.601711289	)	)	;
		real2.add(new Point(	0.473431817	,	1.080235263	)	)	;
		real2.add(new Point(	0.490204148	,	1.088828526	)	)	;
		real2.add(new Point(	0.513681638	,	1.107438641	)	)	;
		real2.add(new Point(	0.540980109	,	1.135202649	)	)	;
		real2.add(new Point(	0.569923167	,	1.158885445	)	)	;
		real2.add(new Point(	0.596699287	,	1.18558189	)	)	;
		real2.add(new Point(	0.626921461	,	1.218141347	)	)	;
		real2.add(new Point(	0.654707004	,	1.262326515	)	)	;
		real2.add(new Point(	0.68778032	,	1.314713552	)	)	;
		real2.add(new Point(	0.730065549	,	1.385841236	)	)	;
		real2.add(new Point(	0.769148686	,	1.453665987	)	)	;
		real2.add(new Point(	0.783064776	,	1.505184406	)	)	;
		real2.add(new Point(	0.797099756	,	1.546716781	)	)	;
		real2.add(new Point(	0.776140378	,	1.545460591	)	)	;
		real2.add(new Point(	0.77784453	,	1.56802547	)	)	;
		real2.add(new Point(	0.752896373	,	1.5585564	)	)	;
		real2.add(new Point(	0.74107317	,	1.582173564	)	)	;
		real2.add(new Point(	0.724758796	,	1.591917669	)	)	;
		real2.add(new Point(	0.455718431	,	1.10435459	)	)	;
		real2.add(new Point(	0.52669757	,	1.266334372	)	)	;

		ArrayList<Point> real1t = new ArrayList<Point>();
		ArrayList<Point> real2t = new ArrayList<Point>();		

		int factor = 250;
		for (int i = 0; i < real1.size(); i++) {
			real1t.add(new Point (real1.get(i).x * factor, real1.get(i).y * factor));
		}
		for (int i = 0; i < real2.size(); i++) {
			real2t.add(new Point (real2.get(i).x * factor, real2.get(i).y * factor));
		}
		

		ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
		arr.add(real1t);
		arr.add(real2t);
		return arr;
	}
	
	public static ArrayList<ArrayList<Point>> getPointSetReal1()
	{
		ArrayList<Point> real1 = new ArrayList<Point>();
		ArrayList<Point> real2 = new ArrayList<Point>();	
		
		real1.add(new Point(126,1308));
		real1.add(new Point(175,1425));
		real1.add(new Point(223,1541));
		real1.add(new Point(272,1654));
		real1.add(new Point(321,1763));
		real1.add(new Point(370,1866));
		real1.add(new Point(418,1963));
		real1.add(new Point(467,2053));
		real1.add(new Point(516,2135));
		real1.add(new Point(565,2207));
		real1.add(new Point(613,2269));
		real1.add(new Point(662,2321));
		real1.add(new Point(711,2361));
		real1.add(new Point(759,2389));
		real1.add(new Point(808,2406));
		real1.add(new Point(857,2410));
		real1.add(new Point(906,2401));
		real1.add(new Point(954,2381));
		real1.add(new Point(1003,2349));
		real1.add(new Point(1052,2305));
		real1.add(new Point(1101,2250));
		real1.add(new Point(1149,2184));
		real1.add(new Point(1198,2109));
		real1.add(new Point(1247,2024));
		real1.add(new Point(1296,1932));
		real1.add(new Point(1344,1832));
		real1.add(new Point(1393,1727));
		real1.add(new Point(1442,1617));
		real1.add(new Point(1490,1503));
		real1.add(new Point(1539,1387));
		real1.add(new Point(1588,1269));
		real1.add(new Point(1637,1152));
		real1.add(new Point(1685,1037));
		real1.add(new Point(1734,924));
		real1.add(new Point(1783,815));
		real1.add(new Point(1832,711));
		real1.add(new Point(1880,614));
		real1.add(new Point(1929,524));
		real1.add(new Point(1978,443));
		real1.add(new Point(2026,371));
		real1.add(new Point(2075,308));
		real1.add(new Point(2124,257));
		real1.add(new Point(2173,217));
		real1.add(new Point(2221,188));
		real1.add(new Point(2270,172));
		real1.add(new Point(2319,168));
		real1.add(new Point(2368,176));
		real1.add(new Point(2416,197));
		real1.add(new Point(2465,229));
		real1.add(new Point(2514,273));
		real1.add(new Point(2562,328));
		real1.add(new Point(2611,394));
		real1.add(new Point(2660,469));
		real1.add(new Point(2709,553));
		real1.add(new Point(2757,646));
		real1.add(new Point(2806,745));
		real1.add(new Point(2855,851));
		real1.add(new Point(2904,961));
		real1.add(new Point(2952,1075));
		real1.add(new Point(3001,1191));

		real2.add(new Point(3757,3513));
		real2.add(new Point(4008,3647));
		real2.add(new Point(4258,3779));
		real2.add(new Point(4502,3909));
		real2.add(new Point(4740,4034));
		real2.add(new Point(4969,4155));
		real2.add(new Point(5188,4268));
		real2.add(new Point(5395,4375));
		real2.add(new Point(5588,4473));
		real2.add(new Point(5765,4562));
		real2.add(new Point(5926,4641));
		real2.add(new Point(6069,4708));
		real2.add(new Point(6194,4765));
		real2.add(new Point(6299,4810));
		real2.add(new Point(6384,4843));
		real2.add(new Point(6449,4863));
		real2.add(new Point(6493,4871));
		real2.add(new Point(6518,4867));
		real2.add(new Point(6522,4851));
		real2.add(new Point(6508,4823));
		real2.add(new Point(6475,4784));
		real2.add(new Point(6424,4734));
		real2.add(new Point(6358,4675));
		real2.add(new Point(6276,4607));
		real2.add(new Point(6181,4530));
		real2.add(new Point(6075,4447));
		real2.add(new Point(5958,4357));
		real2.add(new Point(5834,4263));
		real2.add(new Point(5704,4165));
		real2.add(new Point(5570,4064));
		real2.add(new Point(5434,3963));
		real2.add(new Point(5299,3862));
		real2.add(new Point(5166,3762));
		real2.add(new Point(5037,3665));
		real2.add(new Point(4915,3573));
		real2.add(new Point(4802,3485));
		real2.add(new Point(4699,3404));
		real2.add(new Point(4609,3330));
		real2.add(new Point(4532,3264));
		real2.add(new Point(4471,3208));
		real2.add(new Point(4426,3162));
		real2.add(new Point(4399,3127));
		real2.add(new Point(4391,3103));
		real2.add(new Point(4402,3090));
		real2.add(new Point(4433,3090));
		real2.add(new Point(4484,3103));
		real2.add(new Point(4556,3127));
		real2.add(new Point(4648,3164));
		real2.add(new Point(4759,3213));
		real2.add(new Point(4890,3273));
		real2.add(new Point(5039,3345));
		real2.add(new Point(5206,3427));
		real2.add(new Point(5388,3519));
		real2.add(new Point(5586,3620));
		real2.add(new Point(5797,3729));
		real2.add(new Point(6020,3845));
		real2.add(new Point(6252,3967));
		real2.add(new Point(6492,4094));
		real2.add(new Point(6739,4224));
		real2.add(new Point(6989,4357));

		ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
		arr.add(real1);
		arr.add(real2);
		return arr;
		
	}

	
	public static ArrayList<ArrayList<Point>> getPointSetReal5()
	{
		ArrayList<Point> real1 = new ArrayList<Point>();
		ArrayList<Point> real2 = new ArrayList<Point>();		

		real1.add(new Point(222,1116));
		real1.add(new Point(126,932));
		real1.add(new Point(414,3194));
		real1.add(new Point(414,1391));
		real1.add(new Point(510,1635));
		real1.add(new Point(798,3164));
		real1.add(new Point(702,2033));
		real1.add(new Point(654,1757));
		real1.add(new Point(606,1513));
		real1.add(new Point(798,2583));
		real1.add(new Point(798,2338));
		real1.add(new Point(1277,3744));
		real1.add(new Point(1277,3683));
		real1.add(new Point(1277,3164));
		real1.add(new Point(1853,3744));
		real1.add(new Point(1757,3683));
		real1.add(new Point(1757,3164));
		real1.add(new Point(1757,2980));
		real1.add(new Point(1757,2308));
		real1.add(new Point(1757,2033));
		real1.add(new Point(1757,1727));
		real1.add(new Point(1757,1116));
		real1.add(new Point(1757,810));
		real1.add(new Point(1709,504));
		real1.add(new Point(1709,260));
		real1.add(new Point(1805,2644));
		real1.add(new Point(1805,1421));
		real1.add(new Point(2237,3775));
		real1.add(new Point(2045,3683));
		real1.add(new Point(2189,3133));
		real1.add(new Point(2668,3775));
		real1.add(new Point(2716,3133));
		real1.add(new Point(2620,2338));
		real1.add(new Point(2572,2033));
		real1.add(new Point(2620,1727));
		real1.add(new Point(2716,2552));
		real1.add(new Point(2716,1421));
		real1.add(new Point(2764,1238));
		real1.add(new Point(3148,3133));
		real1.add(new Point(3580,1421));
		real1.add(new Point(3580,1054));
		real1.add(new Point(3580,810));
		real1.add(new Point(3772,1666));
		real1.add(new Point(3676,504));
		real1.add(new Point(3772,3041));
		real1.add(new Point(3772,321));
		real1.add(new Point(3867,3133));
		real1.add(new Point(4011,3072));
		real1.add(new Point(4155,1696));
		real1.add(new Point(4203,993));
		real1.add(new Point(4155,199));
		real1.add(new Point(4251,3133));
		real1.add(new Point(4203,2613));
		real1.add(new Point(4155,2338));
		real1.add(new Point(4299,2124));
		real1.add(new Point(4587,3286));
		real1.add(new Point(4539,2766));
		real1.add(new Point(4635,2033));
		real1.add(new Point(4635,1666));
		real1.add(new Point(4635,1024));
		real1.add(new Point(4635,168));
		real1.add(new Point(4683,2827));
		real1.add(new Point(4827,3500));
		real1.add(new Point(5115,3836));
		real1.add(new Point(5067,3622));
		real1.add(new Point(5115,2827));
		real1.add(new Point(5115,2766));
		real1.add(new Point(5115,1971));
		real1.add(new Point(5115,1666));
		real1.add(new Point(5115,1054));
		real1.add(new Point(5115,199));
		real1.add(new Point(5067,4050));
		real1.add(new Point(5115,1421));
		real1.add(new Point(5163,810));
		real1.add(new Point(5163,504));
		real1.add(new Point(5402,3500));
		real1.add(new Point(5546,2827));
		real1.add(new Point(5642,2766));
		real1.add(new Point(5594,2002));
		real1.add(new Point(5594,1696));
		real1.add(new Point(5594,1024));
		real1.add(new Point(5594,199));
		real1.add(new Point(5642,3286));
		real1.add(new Point(5978,3133));
		real1.add(new Point(6074,2644));
		real1.add(new Point(5978,2094));
		real1.add(new Point(6074,1696));
		real1.add(new Point(6074,1024));
		real1.add(new Point(6074,199));
		real1.add(new Point(6170,3072));
		real1.add(new Point(6266,2277));
		real1.add(new Point(6554,2950));
		real1.add(new Point(6362,2552));
		real1.add(new Point(6362,2369));
		real1.add(new Point(6554,1666));
		real1.add(new Point(6554,1024));
		real1.add(new Point(6458,260));
		real1.add(new Point(6746,443));
		real1.add(new Point(6746,1452));
		real1.add(new Point(6746,688));
		real1.add(new Point(6841,2950));
		real1.add(new Point(6841,1360));
		real1.add(new Point(6889,1116));
		real1.add(new Point(6889,810));
		real1.add(new Point(6841,596));

		
		real2.add(new Point(2301,2005));
		real2.add(new Point(2193,1901));
		real2.add(new Point(2956,2894));
		real2.add(new Point(2494,2177));
		real2.add(new Point(2617,2305));
		real2.add(new Point(3192,3005));
		real2.add(new Point(2842,2525));
		real2.add(new Point(2740,2400));
		real2.add(new Point(2647,2287));
		real2.add(new Point(3044,2774));
		real2.add(new Point(2981,2677));
		real2.add(new Point(3647,3391));
		real2.add(new Point(3631,3367));
		real2.add(new Point(3498,3160));
		real2.add(new Point(4014,3577));
		real2.add(new Point(3937,3522));
		real2.add(new Point(3804,3315));
		real2.add(new Point(3757,3242));
		real2.add(new Point(3584,2975));
		real2.add(new Point(3514,2865));
		real2.add(new Point(3436,2744));
		real2.add(new Point(3279,2501));
		real2.add(new Point(3201,2379));
		real2.add(new Point(3092,2242));
		real2.add(new Point(3029,2145));
		real2.add(new Point(3701,3124));
		real2.add(new Point(3388,2638));
		real2.add(new Point(4266,3713));
		real2.add(new Point(4120,3615));
		real2.add(new Point(4071,3442));
		real2.add(new Point(4541,3852));
		real2.add(new Point(4407,3613));
		real2.add(new Point(4143,3266));
		real2.add(new Point(4034,3129));
		real2.add(new Point(3986,3022));
		real2.add(new Point(4258,3382));
		real2.add(new Point(3969,2932));
		real2.add(new Point(3952,2874));
		real2.add(new Point(4682,3752));
		real2.add(new Point(4519,3211));
		real2.add(new Point(4425,3065));
		real2.add(new Point(4362,2967));
		real2.add(new Point(4704,3370));
		real2.add(new Point(4345,2877));
		real2.add(new Point(5056,3917));
		real2.add(new Point(4359,2835));
		real2.add(new Point(5141,3984));
		real2.add(new Point(5217,4006));
		real2.add(new Point(4956,3506));
		real2.add(new Point(4807,3242));
		real2.add(new Point(4572,2910));
		real2.add(new Point(5385,4108));
		real2.add(new Point(5222,3886));
		real2.add(new Point(5121,3761));
		real2.add(new Point(5158,3722));
		real2.add(new Point(5639,4277));
		real2.add(new Point(5475,4055));
		real2.add(new Point(5348,3794));
		real2.add(new Point(5254,3648));
		real2.add(new Point(5090,3393));
		real2.add(new Point(4870,3053));
		real2.add(new Point(5582,4126));
		real2.add(new Point(5846,4440));
		real2.add(new Point(6116,4666));
		real2.add(new Point(6030,4566));
		real2.add(new Point(5857,4265));
		real2.add(new Point(5842,4241));
		real2.add(new Point(5638,3925));
		real2.add(new Point(5560,3803));
		real2.add(new Point(5403,3560));
		real2.add(new Point(5184,3220));
		real2.add(new Point(6140,4736));
		real2.add(new Point(5497,3706));
		real2.add(new Point(5371,3478));
		real2.add(new Point(5293,3357));
		real2.add(new Point(6213,4625));
		real2.add(new Point(6132,4404));
		real2.add(new Point(6178,4411));
		real2.add(new Point(5951,4092));
		real2.add(new Point(5873,3970));
		real2.add(new Point(5701,3703));
		real2.add(new Point(5489,3375));
		real2.add(new Point(6311,4618));
		real2.add(new Point(6486,4665));
		real2.add(new Point(6422,4502));
		real2.add(new Point(6220,4252));
		real2.add(new Point(6179,4125));
		real2.add(new Point(6007,3858));
		real2.add(new Point(5795,3529));
		real2.add(new Point(6592,4703));
		real2.add(new Point(6450,4418));
		real2.add(new Point(6806,4778));
		real2.add(new Point(6582,4558));
		real2.add(new Point(6535,4485));
		real2.add(new Point(6477,4268));
		real2.add(new Point(6312,4012));
		real2.add(new Point(6055,3678));
		real2.add(new Point(6286,3843));
		real2.add(new Point(6544,4244));
		real2.add(new Point(6348,3941));
		real2.add(new Point(6989,4871));
		real2.add(new Point(6582,4239));
		real2.add(new Point(6550,4157));
		real2.add(new Point(6471,4036));
		real2.add(new Point(6386,3935));

		
		
		ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
		arr.add(real1);
		arr.add(real2);
		return arr;

	}
}

