package bbidder;

public enum Shape {
    _00000013(0,0,0,13),
    _00010012(0,1,0,12),
    _00020011(0,2,0,11),
    _00030010(0,3,0,10),
    _00040009(0,4,0,9),
    _00050008(0,5,0,8),
    _00060007(0,6,0,7),
    _00070006(0,7,0,6),
    _00080005(0,8,0,5),
    _00090004(0,9,0,4),
    _00100003(0,10,0,3),
    _00110002(0,11,0,2),
    _00120001(0,12,0,1),
    _00130000(0,13,0,0),
    _00000112(0,0,1,12),
    _00010111(0,1,1,11),
    _00020110(0,2,1,10),
    _00030109(0,3,1,9),
    _00040108(0,4,1,8),
    _00050107(0,5,1,7),
    _00060106(0,6,1,6),
    _00070105(0,7,1,5),
    _00080104(0,8,1,4),
    _00090103(0,9,1,3),
    _00100102(0,10,1,2),
    _00110101(0,11,1,1),
    _00120100(0,12,1,0),
    _00000211(0,0,2,11),
    _00010210(0,1,2,10),
    _00020209(0,2,2,9),
    _00030208(0,3,2,8),
    _00040207(0,4,2,7),
    _00050206(0,5,2,6),
    _00060205(0,6,2,5),
    _00070204(0,7,2,4),
    _00080203(0,8,2,3),
    _00090202(0,9,2,2),
    _00100201(0,10,2,1),
    _00110200(0,11,2,0),
    _00000310(0,0,3,10),
    _00010309(0,1,3,9),
    _00020308(0,2,3,8),
    _00030307(0,3,3,7),
    _00040306(0,4,3,6),
    _00050305(0,5,3,5),
    _00060304(0,6,3,4),
    _00070303(0,7,3,3),
    _00080302(0,8,3,2),
    _00090301(0,9,3,1),
    _00100300(0,10,3,0),
    _00000409(0,0,4,9),
    _00010408(0,1,4,8),
    _00020407(0,2,4,7),
    _00030406(0,3,4,6),
    _00040405(0,4,4,5),
    _00050404(0,5,4,4),
    _00060403(0,6,4,3),
    _00070402(0,7,4,2),
    _00080401(0,8,4,1),
    _00090400(0,9,4,0),
    _00000508(0,0,5,8),
    _00010507(0,1,5,7),
    _00020506(0,2,5,6),
    _00030505(0,3,5,5),
    _00040504(0,4,5,4),
    _00050503(0,5,5,3),
    _00060502(0,6,5,2),
    _00070501(0,7,5,1),
    _00080500(0,8,5,0),
    _00000607(0,0,6,7),
    _00010606(0,1,6,6),
    _00020605(0,2,6,5),
    _00030604(0,3,6,4),
    _00040603(0,4,6,3),
    _00050602(0,5,6,2),
    _00060601(0,6,6,1),
    _00070600(0,7,6,0),
    _00000706(0,0,7,6),
    _00010705(0,1,7,5),
    _00020704(0,2,7,4),
    _00030703(0,3,7,3),
    _00040702(0,4,7,2),
    _00050701(0,5,7,1),
    _00060700(0,6,7,0),
    _00000805(0,0,8,5),
    _00010804(0,1,8,4),
    _00020803(0,2,8,3),
    _00030802(0,3,8,2),
    _00040801(0,4,8,1),
    _00050800(0,5,8,0),
    _00000904(0,0,9,4),
    _00010903(0,1,9,3),
    _00020902(0,2,9,2),
    _00030901(0,3,9,1),
    _00040900(0,4,9,0),
    _00001003(0,0,10,3),
    _00011002(0,1,10,2),
    _00021001(0,2,10,1),
    _00031000(0,3,10,0),
    _00001102(0,0,11,2),
    _00011101(0,1,11,1),
    _00021100(0,2,11,0),
    _00001201(0,0,12,1),
    _00011200(0,1,12,0),
    _00001300(0,0,13,0),
    _01000012(1,0,0,12),
    _01010011(1,1,0,11),
    _01020010(1,2,0,10),
    _01030009(1,3,0,9),
    _01040008(1,4,0,8),
    _01050007(1,5,0,7),
    _01060006(1,6,0,6),
    _01070005(1,7,0,5),
    _01080004(1,8,0,4),
    _01090003(1,9,0,3),
    _01100002(1,10,0,2),
    _01110001(1,11,0,1),
    _01120000(1,12,0,0),
    _01000111(1,0,1,11),
    _01010110(1,1,1,10),
    _01020109(1,2,1,9),
    _01030108(1,3,1,8),
    _01040107(1,4,1,7),
    _01050106(1,5,1,6),
    _01060105(1,6,1,5),
    _01070104(1,7,1,4),
    _01080103(1,8,1,3),
    _01090102(1,9,1,2),
    _01100101(1,10,1,1),
    _01110100(1,11,1,0),
    _01000210(1,0,2,10),
    _01010209(1,1,2,9),
    _01020208(1,2,2,8),
    _01030207(1,3,2,7),
    _01040206(1,4,2,6),
    _01050205(1,5,2,5),
    _01060204(1,6,2,4),
    _01070203(1,7,2,3),
    _01080202(1,8,2,2),
    _01090201(1,9,2,1),
    _01100200(1,10,2,0),
    _01000309(1,0,3,9),
    _01010308(1,1,3,8),
    _01020307(1,2,3,7),
    _01030306(1,3,3,6),
    _01040305(1,4,3,5),
    _01050304(1,5,3,4),
    _01060303(1,6,3,3),
    _01070302(1,7,3,2),
    _01080301(1,8,3,1),
    _01090300(1,9,3,0),
    _01000408(1,0,4,8),
    _01010407(1,1,4,7),
    _01020406(1,2,4,6),
    _01030405(1,3,4,5),
    _01040404(1,4,4,4),
    _01050403(1,5,4,3),
    _01060402(1,6,4,2),
    _01070401(1,7,4,1),
    _01080400(1,8,4,0),
    _01000507(1,0,5,7),
    _01010506(1,1,5,6),
    _01020505(1,2,5,5),
    _01030504(1,3,5,4),
    _01040503(1,4,5,3),
    _01050502(1,5,5,2),
    _01060501(1,6,5,1),
    _01070500(1,7,5,0),
    _01000606(1,0,6,6),
    _01010605(1,1,6,5),
    _01020604(1,2,6,4),
    _01030603(1,3,6,3),
    _01040602(1,4,6,2),
    _01050601(1,5,6,1),
    _01060600(1,6,6,0),
    _01000705(1,0,7,5),
    _01010704(1,1,7,4),
    _01020703(1,2,7,3),
    _01030702(1,3,7,2),
    _01040701(1,4,7,1),
    _01050700(1,5,7,0),
    _01000804(1,0,8,4),
    _01010803(1,1,8,3),
    _01020802(1,2,8,2),
    _01030801(1,3,8,1),
    _01040800(1,4,8,0),
    _01000903(1,0,9,3),
    _01010902(1,1,9,2),
    _01020901(1,2,9,1),
    _01030900(1,3,9,0),
    _01001002(1,0,10,2),
    _01011001(1,1,10,1),
    _01021000(1,2,10,0),
    _01001101(1,0,11,1),
    _01011100(1,1,11,0),
    _01001200(1,0,12,0),
    _02000011(2,0,0,11),
    _02010010(2,1,0,10),
    _02020009(2,2,0,9),
    _02030008(2,3,0,8),
    _02040007(2,4,0,7),
    _02050006(2,5,0,6),
    _02060005(2,6,0,5),
    _02070004(2,7,0,4),
    _02080003(2,8,0,3),
    _02090002(2,9,0,2),
    _02100001(2,10,0,1),
    _02110000(2,11,0,0),
    _02000110(2,0,1,10),
    _02010109(2,1,1,9),
    _02020108(2,2,1,8),
    _02030107(2,3,1,7),
    _02040106(2,4,1,6),
    _02050105(2,5,1,5),
    _02060104(2,6,1,4),
    _02070103(2,7,1,3),
    _02080102(2,8,1,2),
    _02090101(2,9,1,1),
    _02100100(2,10,1,0),
    _02000209(2,0,2,9),
    _02010208(2,1,2,8),
    _02020207(2,2,2,7),
    _02030206(2,3,2,6),
    _02040205(2,4,2,5),
    _02050204(2,5,2,4),
    _02060203(2,6,2,3),
    _02070202(2,7,2,2),
    _02080201(2,8,2,1),
    _02090200(2,9,2,0),
    _02000308(2,0,3,8),
    _02010307(2,1,3,7),
    _02020306(2,2,3,6),
    _02030305(2,3,3,5),
    _02040304(2,4,3,4),
    _02050303(2,5,3,3),
    _02060302(2,6,3,2),
    _02070301(2,7,3,1),
    _02080300(2,8,3,0),
    _02000407(2,0,4,7),
    _02010406(2,1,4,6),
    _02020405(2,2,4,5),
    _02030404(2,3,4,4),
    _02040403(2,4,4,3),
    _02050402(2,5,4,2),
    _02060401(2,6,4,1),
    _02070400(2,7,4,0),
    _02000506(2,0,5,6),
    _02010505(2,1,5,5),
    _02020504(2,2,5,4),
    _02030503(2,3,5,3),
    _02040502(2,4,5,2),
    _02050501(2,5,5,1),
    _02060500(2,6,5,0),
    _02000605(2,0,6,5),
    _02010604(2,1,6,4),
    _02020603(2,2,6,3),
    _02030602(2,3,6,2),
    _02040601(2,4,6,1),
    _02050600(2,5,6,0),
    _02000704(2,0,7,4),
    _02010703(2,1,7,3),
    _02020702(2,2,7,2),
    _02030701(2,3,7,1),
    _02040700(2,4,7,0),
    _02000803(2,0,8,3),
    _02010802(2,1,8,2),
    _02020801(2,2,8,1),
    _02030800(2,3,8,0),
    _02000902(2,0,9,2),
    _02010901(2,1,9,1),
    _02020900(2,2,9,0),
    _02001001(2,0,10,1),
    _02011000(2,1,10,0),
    _02001100(2,0,11,0),
    _03000010(3,0,0,10),
    _03010009(3,1,0,9),
    _03020008(3,2,0,8),
    _03030007(3,3,0,7),
    _03040006(3,4,0,6),
    _03050005(3,5,0,5),
    _03060004(3,6,0,4),
    _03070003(3,7,0,3),
    _03080002(3,8,0,2),
    _03090001(3,9,0,1),
    _03100000(3,10,0,0),
    _03000109(3,0,1,9),
    _03010108(3,1,1,8),
    _03020107(3,2,1,7),
    _03030106(3,3,1,6),
    _03040105(3,4,1,5),
    _03050104(3,5,1,4),
    _03060103(3,6,1,3),
    _03070102(3,7,1,2),
    _03080101(3,8,1,1),
    _03090100(3,9,1,0),
    _03000208(3,0,2,8),
    _03010207(3,1,2,7),
    _03020206(3,2,2,6),
    _03030205(3,3,2,5),
    _03040204(3,4,2,4),
    _03050203(3,5,2,3),
    _03060202(3,6,2,2),
    _03070201(3,7,2,1),
    _03080200(3,8,2,0),
    _03000307(3,0,3,7),
    _03010306(3,1,3,6),
    _03020305(3,2,3,5),
    _03030304(3,3,3,4),
    _03040303(3,4,3,3),
    _03050302(3,5,3,2),
    _03060301(3,6,3,1),
    _03070300(3,7,3,0),
    _03000406(3,0,4,6),
    _03010405(3,1,4,5),
    _03020404(3,2,4,4),
    _03030403(3,3,4,3),
    _03040402(3,4,4,2),
    _03050401(3,5,4,1),
    _03060400(3,6,4,0),
    _03000505(3,0,5,5),
    _03010504(3,1,5,4),
    _03020503(3,2,5,3),
    _03030502(3,3,5,2),
    _03040501(3,4,5,1),
    _03050500(3,5,5,0),
    _03000604(3,0,6,4),
    _03010603(3,1,6,3),
    _03020602(3,2,6,2),
    _03030601(3,3,6,1),
    _03040600(3,4,6,0),
    _03000703(3,0,7,3),
    _03010702(3,1,7,2),
    _03020701(3,2,7,1),
    _03030700(3,3,7,0),
    _03000802(3,0,8,2),
    _03010801(3,1,8,1),
    _03020800(3,2,8,0),
    _03000901(3,0,9,1),
    _03010900(3,1,9,0),
    _03001000(3,0,10,0),
    _04000009(4,0,0,9),
    _04010008(4,1,0,8),
    _04020007(4,2,0,7),
    _04030006(4,3,0,6),
    _04040005(4,4,0,5),
    _04050004(4,5,0,4),
    _04060003(4,6,0,3),
    _04070002(4,7,0,2),
    _04080001(4,8,0,1),
    _04090000(4,9,0,0),
    _04000108(4,0,1,8),
    _04010107(4,1,1,7),
    _04020106(4,2,1,6),
    _04030105(4,3,1,5),
    _04040104(4,4,1,4),
    _04050103(4,5,1,3),
    _04060102(4,6,1,2),
    _04070101(4,7,1,1),
    _04080100(4,8,1,0),
    _04000207(4,0,2,7),
    _04010206(4,1,2,6),
    _04020205(4,2,2,5),
    _04030204(4,3,2,4),
    _04040203(4,4,2,3),
    _04050202(4,5,2,2),
    _04060201(4,6,2,1),
    _04070200(4,7,2,0),
    _04000306(4,0,3,6),
    _04010305(4,1,3,5),
    _04020304(4,2,3,4),
    _04030303(4,3,3,3),
    _04040302(4,4,3,2),
    _04050301(4,5,3,1),
    _04060300(4,6,3,0),
    _04000405(4,0,4,5),
    _04010404(4,1,4,4),
    _04020403(4,2,4,3),
    _04030402(4,3,4,2),
    _04040401(4,4,4,1),
    _04050400(4,5,4,0),
    _04000504(4,0,5,4),
    _04010503(4,1,5,3),
    _04020502(4,2,5,2),
    _04030501(4,3,5,1),
    _04040500(4,4,5,0),
    _04000603(4,0,6,3),
    _04010602(4,1,6,2),
    _04020601(4,2,6,1),
    _04030600(4,3,6,0),
    _04000702(4,0,7,2),
    _04010701(4,1,7,1),
    _04020700(4,2,7,0),
    _04000801(4,0,8,1),
    _04010800(4,1,8,0),
    _04000900(4,0,9,0),
    _05000008(5,0,0,8),
    _05010007(5,1,0,7),
    _05020006(5,2,0,6),
    _05030005(5,3,0,5),
    _05040004(5,4,0,4),
    _05050003(5,5,0,3),
    _05060002(5,6,0,2),
    _05070001(5,7,0,1),
    _05080000(5,8,0,0),
    _05000107(5,0,1,7),
    _05010106(5,1,1,6),
    _05020105(5,2,1,5),
    _05030104(5,3,1,4),
    _05040103(5,4,1,3),
    _05050102(5,5,1,2),
    _05060101(5,6,1,1),
    _05070100(5,7,1,0),
    _05000206(5,0,2,6),
    _05010205(5,1,2,5),
    _05020204(5,2,2,4),
    _05030203(5,3,2,3),
    _05040202(5,4,2,2),
    _05050201(5,5,2,1),
    _05060200(5,6,2,0),
    _05000305(5,0,3,5),
    _05010304(5,1,3,4),
    _05020303(5,2,3,3),
    _05030302(5,3,3,2),
    _05040301(5,4,3,1),
    _05050300(5,5,3,0),
    _05000404(5,0,4,4),
    _05010403(5,1,4,3),
    _05020402(5,2,4,2),
    _05030401(5,3,4,1),
    _05040400(5,4,4,0),
    _05000503(5,0,5,3),
    _05010502(5,1,5,2),
    _05020501(5,2,5,1),
    _05030500(5,3,5,0),
    _05000602(5,0,6,2),
    _05010601(5,1,6,1),
    _05020600(5,2,6,0),
    _05000701(5,0,7,1),
    _05010700(5,1,7,0),
    _05000800(5,0,8,0),
    _06000007(6,0,0,7),
    _06010006(6,1,0,6),
    _06020005(6,2,0,5),
    _06030004(6,3,0,4),
    _06040003(6,4,0,3),
    _06050002(6,5,0,2),
    _06060001(6,6,0,1),
    _06070000(6,7,0,0),
    _06000106(6,0,1,6),
    _06010105(6,1,1,5),
    _06020104(6,2,1,4),
    _06030103(6,3,1,3),
    _06040102(6,4,1,2),
    _06050101(6,5,1,1),
    _06060100(6,6,1,0),
    _06000205(6,0,2,5),
    _06010204(6,1,2,4),
    _06020203(6,2,2,3),
    _06030202(6,3,2,2),
    _06040201(6,4,2,1),
    _06050200(6,5,2,0),
    _06000304(6,0,3,4),
    _06010303(6,1,3,3),
    _06020302(6,2,3,2),
    _06030301(6,3,3,1),
    _06040300(6,4,3,0),
    _06000403(6,0,4,3),
    _06010402(6,1,4,2),
    _06020401(6,2,4,1),
    _06030400(6,3,4,0),
    _06000502(6,0,5,2),
    _06010501(6,1,5,1),
    _06020500(6,2,5,0),
    _06000601(6,0,6,1),
    _06010600(6,1,6,0),
    _06000700(6,0,7,0),
    _07000006(7,0,0,6),
    _07010005(7,1,0,5),
    _07020004(7,2,0,4),
    _07030003(7,3,0,3),
    _07040002(7,4,0,2),
    _07050001(7,5,0,1),
    _07060000(7,6,0,0),
    _07000105(7,0,1,5),
    _07010104(7,1,1,4),
    _07020103(7,2,1,3),
    _07030102(7,3,1,2),
    _07040101(7,4,1,1),
    _07050100(7,5,1,0),
    _07000204(7,0,2,4),
    _07010203(7,1,2,3),
    _07020202(7,2,2,2),
    _07030201(7,3,2,1),
    _07040200(7,4,2,0),
    _07000303(7,0,3,3),
    _07010302(7,1,3,2),
    _07020301(7,2,3,1),
    _07030300(7,3,3,0),
    _07000402(7,0,4,2),
    _07010401(7,1,4,1),
    _07020400(7,2,4,0),
    _07000501(7,0,5,1),
    _07010500(7,1,5,0),
    _07000600(7,0,6,0),
    _08000005(8,0,0,5),
    _08010004(8,1,0,4),
    _08020003(8,2,0,3),
    _08030002(8,3,0,2),
    _08040001(8,4,0,1),
    _08050000(8,5,0,0),
    _08000104(8,0,1,4),
    _08010103(8,1,1,3),
    _08020102(8,2,1,2),
    _08030101(8,3,1,1),
    _08040100(8,4,1,0),
    _08000203(8,0,2,3),
    _08010202(8,1,2,2),
    _08020201(8,2,2,1),
    _08030200(8,3,2,0),
    _08000302(8,0,3,2),
    _08010301(8,1,3,1),
    _08020300(8,2,3,0),
    _08000401(8,0,4,1),
    _08010400(8,1,4,0),
    _08000500(8,0,5,0),
    _09000004(9,0,0,4),
    _09010003(9,1,0,3),
    _09020002(9,2,0,2),
    _09030001(9,3,0,1),
    _09040000(9,4,0,0),
    _09000103(9,0,1,3),
    _09010102(9,1,1,2),
    _09020101(9,2,1,1),
    _09030100(9,3,1,0),
    _09000202(9,0,2,2),
    _09010201(9,1,2,1),
    _09020200(9,2,2,0),
    _09000301(9,0,3,1),
    _09010300(9,1,3,0),
    _09000400(9,0,4,0),
    _10000003(10,0,0,3),
    _10010002(10,1,0,2),
    _10020001(10,2,0,1),
    _10030000(10,3,0,0),
    _10000102(10,0,1,2),
    _10010101(10,1,1,1),
    _10020100(10,2,1,0),
    _10000201(10,0,2,1),
    _10010200(10,1,2,0),
    _10000300(10,0,3,0),
    _11000002(11,0,0,2),
    _11010001(11,1,0,1),
    _11020000(11,2,0,0),
    _11000101(11,0,1,1),
    _11010100(11,1,1,0),
    _11000200(11,0,2,0),
    _12000001(12,0,0,1),
    _12010000(12,1,0,0),
    _12000100(12,0,1,0),
    _13000000(13,0,0,0);
    
    final long num;
    
    public int numInSuit(int suit) {
        return (int)((num >> (suit * 16)) & 0xffff);
    }

    private Shape(int ... num) {
        this.num = toNum(num);
    }
    
    public static long toNum(int ... num) {
        return (((long)num[3]) << (16 * 3)) | (((long)num[2]) << (16 * 2))  | (((long)num[1]) << (16 * 1)) | num[0];
    }
    
    static final Shape[][][] numToShape = new Shape[14][14][14];
            
    static {
        for(Shape s: Shape.values()) {
            numToShape[s.numInSuit(0)][s.numInSuit(1)][s.numInSuit(2)] = s;
        }
    }
    
    public static Shape getShape(int ... num) {
        Shape shape = numToShape[num[0]][num[1]][num[2]];
        if (shape == null) {
            throw new IllegalArgumentException();
        }
        return shape;
    }
    
    public boolean isBalanced() {
        int ndoub = 0;
        for (int suit = 0; suit < 4; suit++) {
            int len = numInSuit(suit);
            if (len < 2) {
                return false;
            }
            if (len == 2) {
                ndoub++;
            }
        }
        return ndoub <= 1;
    }
    
    public boolean isSuitInRange(int suit, Range rng) {
        return rng.contains(numInSuit(suit));
    }
    

    public boolean isLongerOrEqual(int suit, int among) {
        int len = numInSuit(suit);
        for (int s : BitUtil.iterate(among)) {
            int len2 = numInSuit(s);
            if (len2 > len) {
                return false;
            }
        }
        return true;
    }
}
