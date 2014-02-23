package Sensor;

import Computer.BordComputer;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Sensors implements Runnable {

	private BordComputer bordComputer;

	public Sensors(BordComputer bordComputer) {
		this.bordComputer = bordComputer;
	}

	private void waitOneSecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public void run() {	
		
		int hallEffect = 0;
		double injection = 1.0;
		double volume = 45.0;
		int latIndex = 0;
		int longIndex = 0;
		
		while(true) {
			
			//Hall effect
			bordComputer.computeInstantaneousSpeed(hallEffect);
			hallEffect++;
			
			//injection
			bordComputer.computeInstantaneousConsumption(injection);
			
			//volume:
			bordComputer.computeEssenceVolumeDisponible(volume);
			
			if (volume - injection >= 0) {
				volume -= injection / 1000;
			}

			//position:
			if ((latIndex >= latitude.length) || (longIndex >= longitude.length)) {
				latIndex = 0;
				longIndex = 0;
			}
			
			bordComputer.computePosition(latitude[latIndex], longitude[longIndex]);
			latIndex++;
			longIndex++;
			
			waitOneSecond();
		}
	}

	double latitude[] = { 46.326548, 46.326549, 46.32656, 46.326603, 46.326695,
			46.326753, 46.32684, 46.326974, 46.32705, 46.327147, 46.327229,
			46.327255, 46.327228, 46.327234, 46.327278, 46.327319, 46.327392,
			46.327458, 46.327539, 46.327624, 46.327709, 46.327795, 46.327862,
			46.327943, 46.328034, 46.328111, 46.328173, 46.32824, 46.328294,
			46.328379, 46.328468, 46.328544, 46.328558, 46.328611, 46.32865,
			46.328654, 46.328679, 46.328736, 46.328832, 46.328754, 46.328635,
			46.328675, 46.328696, 46.328701, 46.328716, 46.328677, 46.328608,
			46.328545, 46.328517, 46.328491, 46.328469, 46.32845, 46.328448,
			46.328444, 46.328386, 46.328363, 46.328351, 46.328307, 46.328259,
			46.328234, 46.328175, 46.328191, 46.328204, 46.328185, 46.328157,
			46.328127, 46.328106, 46.328021, 46.327898, 46.327862, 46.327776,
			46.327728, 46.32771, 46.327683, 46.327666, 46.32758, 46.327546,
			46.327528, 46.327502, 46.32749, 46.327473, 46.327442, 46.327435,
			46.327439, 46.327507, 46.327558, 46.327591, 46.327621, 46.327637,
			46.327661, 46.327633, 46.327574, 46.327563, 46.32755, 46.327514,
			46.327485, 46.327449, 46.327402, 46.327357, 46.32732, 46.327284,
			46.327263, 46.327245, 46.327208, 46.327218, 46.327271, 46.327258,
			46.327262, 46.327287, 46.327313, 46.327365, 46.327421, 46.327495,
			46.327558, 46.327635, 46.327697, 46.327771, 46.327854, 46.327948,
			46.328055, 46.328162, 46.328218, 46.328242, 46.328273, 46.328349,
			46.328427, 46.32851, 46.328421, 46.328426, 46.328417, 46.328403,
			46.328417, 46.328475, 46.328569, 46.328654, 46.328827, 46.328941,
			46.329038, 46.329111, 46.329184, 46.32926, 46.32934, 46.329406,
			46.329461, 46.329514, 46.329551, 46.329556, 46.329573, 46.329571,
			46.329536, 46.32947, 46.329428, 46.329389, 46.329426, 46.329452,
			46.329456, 46.329467, 46.329488, 46.329516, 46.329557, 46.329616,
			46.329677, 46.32976, 46.329821, 46.329854, 46.329874, 46.329835,
			46.329798, 46.329805, 46.3298, 46.329782, 46.329794, 46.32984,
			46.329868, 46.329928, 46.330001, 46.330059, 46.330216, 46.330314,
			46.330445, 46.330584, 46.330712, 46.330841, 46.330932, 46.330998,
			46.331087, 46.331158, 46.33121, 46.331244, 46.331255, 46.331261,
			46.331282, 46.331313, 46.331401, 46.331366, 46.331446, 46.331546,
			46.331688, 46.331868, 46.33203, 46.332175, 46.332325, 46.332324,
			46.332392, 46.332443, 46.332486, 46.332501, 46.332537, 46.332605,
			46.332633, 46.332646, 46.332646, 46.332608, 46.332541, 46.332581,
			46.332606, 46.332576, 46.332557, 46.332547, 46.332539, 46.332541,
			46.332532, 46.332526, 46.332516, 46.332505, 46.332493, 46.332471,
			46.33246, 46.332451, 46.332442, 46.332421, 46.332398, 46.33239,
			46.332374, 46.332356, 46.332337, 46.332322, 46.332301, 46.332283,
			46.332258, 46.332243, 46.332235, 46.332225, 46.332214, 46.332199,
			46.332179, 46.332161, 46.332145, 46.332133, 46.332116, 46.3321,
			46.332081, 46.332069, 46.332056, 46.332043, 46.332026, 46.332009,
			46.331994, 46.331984, 46.331968, 46.331954, 46.331938, 46.331921,
			46.331903, 46.331886, 46.331867, 46.331849, 46.331831, 46.331813,
			46.331795, 46.331775, 46.331756, 46.331736, 46.331716, 46.331698,
			46.331675, 46.33166, 46.331636, 46.331611, 46.331583, 46.331574,
			46.331564, 46.33155, 46.331538, 46.331516, 46.331503, 46.331483,
			46.331457, 46.331439, 46.331422, 46.331405, 46.33139, 46.331381,
			46.331371, 46.331355, 46.331326, 46.331311, 46.331288, 46.331276,
			46.331257, 46.331251, 46.331232, 46.331211, 46.331199, 46.331192,
			46.331167, 46.331144, 46.331121, 46.331099, 46.331081, 46.331067,
			46.331053, 46.331038, 46.331021, 46.331, 46.330988, 46.330976,
			46.330964, 46.330945, 46.330929, 46.33092, 46.330906, 46.330897,
			46.330889, 46.330879, 46.330868, 46.330859, 46.330865, 46.330842,
			46.330842, 46.330819, 46.330788, 46.33077, 46.330752, 46.330741,
			46.330729, 46.330716, 46.330704, 46.330696, 46.330684, 46.330672,
			46.330654, 46.330633, 46.330612, 46.330596, 46.33059, 46.330584,
			46.330575, 46.330558, 46.330532, 46.330502, 46.33048, 46.330457,
			46.330442, 46.330416, 46.330391, 46.330368, 46.330347, 46.330325,
			46.330302, 46.330285, 46.330256, 46.330238, 46.330222, 46.330208,
			46.330195, 46.330177, 46.330163, 46.330149, 46.330142, 46.330132,
			46.330127, 46.330123, 46.330128, 46.330133, 46.330139, 46.330145,
			46.33015, 46.330158, 46.330153, 46.330143, 46.330135, 46.330129,
			46.330141, 46.330141, 46.330143, 46.330116, 46.330114, 46.330081,
			46.330045, 46.330036, 46.330043, 46.330028, 46.330021, 46.330011,
			46.329997, 46.329986, 46.329975, 46.329963, 46.329948, 46.329943,
			46.329932, 46.329904, 46.329873, 46.329848, 46.329824, 46.329793,
			46.329772, 46.329713, 46.329682, 46.329653, 46.329644, 46.329636,
			46.329636, 46.329625, 46.329617, 46.329606, 46.329592, 46.329576,
			46.329572, 46.329553, 46.329534, 46.329524, 46.329506, 46.329484,
			46.329468, 46.329453, 46.329433, 46.329417, 46.329386, 46.329366,
			46.329347, 46.329326, 46.329304, 46.329281, 46.329261, 46.329244,
			46.329238, 46.329234, 46.329223, 46.329212, 46.329203, 46.329194,
			46.329175, 46.329162, 46.329145, 46.32913, 46.329113, 46.329093,
			46.329069, 46.329056, 46.329036, 46.329027, 46.329007, 46.328988,
			46.328963, 46.328954, 46.328948, 46.328934, 46.32891, 46.328886,
			46.32894, 46.328952, 46.328908, 46.328883, 46.328855, 46.328829,
			46.328803, 46.32878, 46.328756, 46.328725, 46.328665, 46.328639,
			46.328602, 46.328573, 46.328536, 46.328514, 46.328486, 46.328459,
			46.328432, 46.328421, 46.328406, 46.328389, 46.328361, 46.32835,
			46.328311, 46.328291, 46.328262, 46.32824, 46.328208, 46.328178,
			46.32816, 46.328129, 46.328114, 46.328097, 46.32808, 46.328062,
			46.328042, 46.328023, 46.328003 };

	double longitude[] = { 7.401713, 7.401636, 7.401566, 7.401457, 7.401274,
			7.401151, 7.40101, 7.400851, 7.400726, 7.400651, 7.400583,
			7.400519, 7.400599, 7.400681, 7.400706, 7.400736, 7.400789,
			7.40086, 7.400966, 7.401123, 7.401327, 7.401572, 7.401929,
			7.402233, 7.402544, 7.40287, 7.403194, 7.403526, 7.403879,
			7.404221, 7.404565, 7.404922, 7.405292, 7.405662, 7.40602,
			7.406393, 7.406721, 7.407004, 7.407287, 7.407551, 7.407737,
			7.407929, 7.408129, 7.4083, 7.408378, 7.40847, 7.408587, 7.408692,
			7.408787, 7.408891, 7.409011, 7.40914, 7.409243, 7.409349,
			7.409466, 7.409564, 7.409684, 7.409869, 7.41007, 7.410227,
			7.410417, 7.410581, 7.410765, 7.410999, 7.411206, 7.411416,
			7.411629, 7.411831, 7.412014, 7.412233, 7.412439, 7.412663,
			7.412889, 7.413125, 7.41339, 7.413721, 7.414005, 7.414313,
			7.414598, 7.414881, 7.415062, 7.415285, 7.415489, 7.415711,
			7.415867, 7.416038, 7.416183, 7.416306, 7.416436, 7.416555,
			7.416656, 7.416834, 7.416993, 7.417163, 7.417344, 7.417542,
			7.417762, 7.417983, 7.418208, 7.418443, 7.418666, 7.418893,
			7.419083, 7.419281, 7.41936, 7.419417, 7.419537, 7.419633,
			7.419774, 7.419927, 7.420124, 7.420341, 7.420544, 7.420776,
			7.420986, 7.421207, 7.421445, 7.421693, 7.421938, 7.422164,
			7.422397, 7.422621, 7.422871, 7.423132, 7.423366, 7.423594,
			7.423833, 7.424084, 7.424318, 7.424537, 7.424772, 7.425033,
			7.425265, 7.425462, 7.425631, 7.425729, 7.425915, 7.426115,
			7.426309, 7.426496, 7.426675, 7.426826, 7.426983, 7.427151,
			7.427298, 7.42744, 7.427576, 7.427718, 7.427868, 7.428014,
			7.428142, 7.428294, 7.428455, 7.428611, 7.428764, 7.428933,
			7.429129, 7.429356, 7.429612, 7.429872, 7.43014, 7.430371, 7.4306,
			7.430805, 7.431007, 7.431185, 7.431377, 7.431555, 7.431751,
			7.43193, 7.432131, 7.432371, 7.432598, 7.432796, 7.432967,
			7.433111, 7.433196, 7.433147, 7.433217, 7.433235, 7.433254,
			7.433277, 7.433303, 7.433328, 7.433353, 7.43336, 7.433375,
			7.433464, 7.433608, 7.433802, 7.434043, 7.434304, 7.434542,
			7.434764, 7.434966, 7.435136, 7.43526, 7.435325, 7.43535, 7.435381,
			7.435454, 7.435522, 7.435749, 7.43589, 7.43603, 7.436163, 7.436303,
			7.43639, 7.436462, 7.436563, 7.436653, 7.436737, 7.436806,
			7.436778, 7.436592, 7.43644, 7.436353, 7.436238, 7.436123,
			7.436019, 7.435889, 7.435819, 7.435742, 7.435665, 7.43559,
			7.435513, 7.435386, 7.43532, 7.435247, 7.435178, 7.435053,
			7.434932, 7.434807, 7.434684, 7.43457, 7.434454, 7.434339,
			7.434222, 7.434106, 7.433986, 7.433873, 7.433721, 7.433604,
			7.43349, 7.433368, 7.433247, 7.433134, 7.433025, 7.432913,
			7.432798, 7.432685, 7.432571, 7.43246, 7.432349, 7.432239,
			7.432127, 7.432016, 7.43191, 7.431805, 7.431682, 7.431559,
			7.431434, 7.431312, 7.431191, 7.431074, 7.430954, 7.430836,
			7.430715, 7.430597, 7.430477, 7.430352, 7.430227, 7.430103,
			7.42998, 7.429855, 7.429729, 7.429665, 7.42954, 7.429417, 7.429288,
			7.429224, 7.429158, 7.429043, 7.428978, 7.428855, 7.42874,
			7.428619, 7.428497, 7.428378, 7.428258, 7.428135, 7.428017,
			7.427908, 7.427843, 7.427727, 7.427592, 7.427466, 7.427345,
			7.427276, 7.427146, 7.427079, 7.426956, 7.426847, 7.426736,
			7.426665, 7.426554, 7.426448, 7.426335, 7.426222, 7.426111,
			7.425994, 7.42588, 7.425759, 7.425638, 7.42551, 7.425444, 7.425377,
			7.425313, 7.42519, 7.425071, 7.424958, 7.424842, 7.424778,
			7.424711, 7.424643, 7.424574, 7.424484, 7.424414, 7.424319,
			7.424215, 7.424112, 7.424006, 7.42389, 7.423774, 7.423675,
			7.423591, 7.423515, 7.423443, 7.423375, 7.423305, 7.423238,
			7.423137, 7.423046, 7.422945, 7.42285, 7.42278, 7.422691, 7.422583,
			7.422478, 7.422369, 7.422249, 7.422137, 7.422023, 7.42196,
			7.421836, 7.421716, 7.421602, 7.421495, 7.421379, 7.42127, 7.42117,
			7.421081, 7.420999, 7.420912, 7.42082, 7.420728, 7.420628, 7.42052,
			7.420399, 7.420273, 7.420149, 7.420028, 7.419911, 7.419789,
			7.419724, 7.419601, 7.419536, 7.419466, 7.419339, 7.419207,
			7.419082, 7.419006, 7.418935, 7.418795, 7.418711, 7.41864,
			7.418551, 7.418464, 7.418402, 7.41832, 7.418245, 7.418161,
			7.418088, 7.418009, 7.417889, 7.417767, 7.41765, 7.417529,
			7.417411, 7.417288, 7.417223, 7.417159, 7.417043, 7.416936,
			7.416828, 7.416714, 7.416603, 7.416482, 7.416335, 7.416199,
			7.41609, 7.416012, 7.415944, 7.415866, 7.415802, 7.415695,
			7.415618, 7.41549, 7.415371, 7.415286, 7.415162, 7.415038,
			7.414974, 7.414849, 7.414723, 7.414597, 7.414478, 7.414354,
			7.414234, 7.414133, 7.414022, 7.413908, 7.413779, 7.413646,
			7.413513, 7.413357, 7.41328, 7.413147, 7.413027, 7.412963,
			7.412839, 7.412699, 7.412633, 7.412489, 7.412423, 7.412297,
			7.412229, 7.412113, 7.412045, 7.411928, 7.411833, 7.41177,
			7.411653, 7.41154, 7.411411, 7.411305, 7.411185, 7.41112, 7.411002,
			7.410895, 7.41082, 7.410787, 7.410719, 7.410604, 7.410527,
			7.410446, 7.410357, 7.41027, 7.410188, 7.410107, 7.410009,
			7.409821, 7.409751, 7.409634, 7.409519, 7.409401, 7.409337,
			7.409275, 7.409166, 7.409054, 7.408984, 7.408902, 7.408835,
			7.408715, 7.408649, 7.408536, 7.408476, 7.408348, 7.408228,
			7.408113, 7.40799, 7.40793, 7.407809, 7.407746, 7.407683, 7.40762,
			7.407556, 7.407492, 7.407431, 7.407368, };

}