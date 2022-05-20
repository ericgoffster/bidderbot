package bbidder;

import bbidder.utils.BitUtil;

/**
 * Represents a particular suit shape.
 * 
 * @author goffster
 *
 */
public enum Shape {
    _03070003(3, 7, 0, 3, 0.000221730),
    _00020605(0, 2, 6, 5, 0.000270860),
    _00030703(0, 3, 7, 3, 0.000221210),
    _00080500(0, 8, 5, 0, 2.65000e-06),
    _06000007(6, 0, 0, 7, 4.88000e-06),
    _08000500(8, 0, 5, 0, 2.79000e-06),
    _06010600(6, 1, 6, 0, 6.05600e-05),
    _00011002(0, 1, 10, 2, 5.60000e-07),
    _05010700(5, 1, 7, 0, 4.49600e-05),
    _04010701(4, 1, 7, 1, 0.000326570),
    _00010705(0, 1, 7, 5, 4.65900e-05),
    _02040601(2, 4, 6, 1, 0.00196412),
    _04040203(4, 4, 2, 3, 0.0179659),
    _04020007(4, 2, 0, 7, 0.000152260),
    _03010108(3, 1, 1, 8, 9.79900e-05),
    _04010503(4, 1, 5, 3, 0.00538228),
    _05020303(5, 2, 3, 3, 0.0129217),
    _04000702(4, 0, 7, 2, 0.000151770),
    _02010604(2, 1, 6, 4, 0.00195550),
    _02050501(2, 5, 5, 1, 0.00264186),
    _00060502(0, 6, 5, 2, 0.000272180),
    _06010402(6, 1, 4, 2, 0.00196161),
    _01010308(1, 1, 3, 8, 9.60800e-05),
    _01000507(1, 0, 5, 7, 4.53400e-05),
    _09020101(9, 2, 1, 1, 1.45900e-05),
    _02050303(2, 5, 3, 3, 0.0129316),
    _02000605(2, 0, 6, 5, 0.000272970),
    _04030204(4, 3, 2, 4, 0.0179754),
    _02060104(2, 6, 1, 4, 0.00195295),
    _02010307(2, 1, 3, 7, 0.000786170),
    _01070500(1, 7, 5, 0, 4.61800e-05),
    _02060302(2, 6, 3, 2, 0.00469083),
    _05010007(5, 1, 0, 7, 4.51600e-05),
    _00040108(0, 4, 1, 8, 1.88600e-05),
    _02080300(2, 8, 3, 0, 4.50100e-05),
    _07030102(7, 3, 1, 2, 0.000789010),
    _10000201(10, 0, 2, 1, 5.70000e-07),
    _02010208(2, 1, 2, 8, 0.000161610),
    _11000002(11, 0, 0, 2, 2.00000e-08),
    _01000408(1, 0, 4, 8, 1.95900e-05),
    _00070303(0, 7, 3, 3, 0.000220110),
    _01070104(1, 7, 1, 4, 0.000324650),
    _01000705(1, 0, 7, 5, 4.35400e-05),
    _01040107(1, 4, 1, 7, 0.000323570),
    _05080000(5, 8, 0, 0, 2.61000e-06),
    _05010106(5, 1, 1, 6, 0.000589160),
    _03030403(3, 3, 4, 3, 0.0263515),
    _07020202(7, 2, 2, 2, 0.00127986),
    _00040207(0, 4, 2, 7, 0.000150620),
    _03050104(3, 5, 1, 4, 0.00539302),
    _00000013(0, 0, 0, 13, 0.00000),
    _02040007(2, 4, 0, 7, 0.000152490),
    _03040006(3, 4, 0, 6, 0.000555130),
    _11020000(11, 2, 0, 0, 2.00000e-08),
    _05030401(5, 3, 4, 1, 0.00537246),
    _00001300(0, 0, 13, 0, 0.00000),
    _05010502(5, 1, 5, 2, 0.00264512),
    _00090004(0, 9, 0, 4, 8.20000e-07),
    _02040700(2, 4, 7, 0, 0.000150080),
    _00021001(0, 2, 10, 1, 4.80000e-07),
    _00050107(0, 5, 1, 7, 4.56700e-05),
    _00001102(0, 0, 11, 2, 0.00000),
    _09030100(9, 3, 1, 0, 4.33000e-06),
    _00020506(0, 2, 5, 6, 0.000267800),
    _03040204(3, 4, 2, 4, 0.0179782),
    _07020400(7, 2, 4, 0, 0.000150350),
    _00030010(0, 3, 0, 10, 9.00000e-08),
    _02080102(2, 8, 1, 2, 0.000160560),
    _08010301(8, 1, 3, 1, 9.59500e-05),
    _03020107(3, 2, 1, 7, 0.000789570),
    _09030001(9, 3, 0, 1, 4.15000e-06),
    _05060200(5, 6, 2, 0, 0.000273710),
    _03050203(3, 5, 2, 3, 0.0129016),
    _00060106(0, 6, 1, 6, 6.00300e-05),
    _07010104(7, 1, 1, 4, 0.000322760),
    _02020009(2, 2, 0, 9, 6.97000e-06),
    _02000110(2, 0, 1, 10, 5.10000e-07),
    _09010003(9, 1, 0, 3, 4.20000e-06),
    _00090400(0, 9, 4, 0, 7.20000e-07),
    _06040102(6, 4, 1, 2, 0.00196607),
    _06070000(6, 7, 0, 0, 4.95000e-06),
    _00070204(0, 7, 2, 4, 0.000152360),
    _08010103(8, 1, 1, 3, 9.76200e-05),
    _05030005(5, 3, 0, 5, 0.000743980),
    _00020308(0, 2, 3, 8, 4.50200e-05),
    _00010309(0, 1, 3, 9, 4.07000e-06),
    _02010802(2, 1, 8, 2, 0.000160130),
    _09000301(9, 0, 3, 1, 4.14000e-06),
    _02030503(2, 3, 5, 3, 0.0129305),
    _02000308(2, 0, 3, 8, 4.52900e-05),
    _00030604(0, 3, 6, 4, 0.000552320),
    _00010804(0, 1, 8, 4, 1.86600e-05),
    _01070401(1, 7, 4, 1, 0.000327650),
    _02100100(2, 10, 1, 0, 4.90000e-07),
    _02070400(2, 7, 4, 0, 0.000150580),
    _00040801(0, 4, 8, 1, 1.88600e-05),
    _07040101(7, 4, 1, 1, 0.000324780),
    _06040300(6, 4, 3, 0, 0.000553860),
    _01030108(1, 3, 1, 8, 9.77300e-05),
    _09020200(9, 2, 2, 0, 6.64000e-06),
    _00030109(0, 3, 1, 9, 4.24000e-06),
    _01040305(1, 4, 3, 5, 0.00539412),
    _05000206(5, 0, 2, 6, 0.000271190),
    _03040303(3, 4, 3, 3, 0.0263320),
    _05000404(5, 0, 4, 4, 0.00103525),
    _03070300(3, 7, 3, 0, 0.000220080),
    _03030205(3, 3, 2, 5, 0.0129450),
    _04030501(4, 3, 5, 1, 0.00539369),
    _02020504(2, 2, 5, 4, 0.00880360),
    _02050006(2, 5, 0, 6, 0.000269610),
    _00000112(0, 0, 1, 12, 0.00000),
    _01010704(1, 1, 7, 4, 0.000325500),
    _07050001(7, 5, 0, 1, 4.48800e-05),
    _00011200(0, 1, 12, 0, 0.00000),
    _09020002(9, 2, 0, 2, 6.96000e-06),
    _00050503(0, 5, 5, 3, 0.000745690),
    _02040304(2, 4, 3, 4, 0.0179770),
    _02060005(2, 6, 0, 5, 0.000272970),
    _04020700(4, 2, 7, 0, 0.000149680),
    _06000304(6, 0, 3, 4, 0.000547660),
    _04000108(4, 0, 1, 8, 1.90400e-05),
    _00060700(0, 6, 7, 0, 4.85000e-06),
    _03060301(3, 6, 3, 1, 0.00287778),
    _00010507(0, 1, 5, 7, 4.45300e-05),
    _02080201(2, 8, 2, 1, 0.000158420),
    _04030402(4, 3, 4, 2, 0.0179519),
    _01020802(1, 2, 8, 2, 0.000161200),
    _02110000(2, 11, 0, 0, 5.00000e-08),
    _02070301(2, 7, 3, 1, 0.000789620),
    _03020404(3, 2, 4, 4, 0.0179734),
    _02020306(2, 2, 3, 6, 0.00471018),
    _00050602(0, 5, 6, 2, 0.000271540),
    _04060201(4, 6, 2, 1, 0.00195735),
    _01070203(1, 7, 2, 3, 0.000779640),
    _01000309(1, 0, 3, 9, 4.38000e-06),
    _05050102(5, 5, 1, 2, 0.00265289),
    _00120100(0, 12, 1, 0, 0.00000),
    _00130000(0, 13, 0, 0, 0.00000),
    _06000700(6, 0, 7, 0, 4.82000e-06),
    _01030702(1, 3, 7, 2, 0.000782330),
    _02090101(2, 9, 1, 1, 1.46800e-05),
    _00080104(0, 8, 1, 4, 1.86400e-05),
    _00010903(0, 1, 9, 3, 4.45000e-06),
    _03030106(3, 3, 1, 6, 0.00287717),
    _01110100(1, 11, 1, 0, 2.00000e-08),
    _04070002(4, 7, 0, 2, 0.000149810),
    _03090100(3, 9, 1, 0, 3.74000e-06),
    _01000012(1, 0, 0, 12, 0.00000),
    _00070600(0, 7, 6, 0, 4.88000e-06),
    _01000111(1, 0, 1, 11, 2.00000e-08),
    _06010006(6, 1, 0, 6, 6.00400e-05),
    _02050600(2, 5, 6, 0, 0.000269250),
    _04010602(4, 1, 6, 2, 0.00196071),
    _07000600(7, 0, 6, 0, 4.66000e-06),
    _01020406(1, 2, 4, 6, 0.00195349),
    _03060400(3, 6, 4, 0, 0.000548640),
    _07050100(7, 5, 1, 0, 4.43000e-05),
    _01080400(1, 8, 4, 0, 1.85900e-05),
    _01030603(1, 3, 6, 3, 0.00287867),
    _01090102(1, 9, 1, 2, 1.48000e-05),
    _00020407(0, 2, 4, 7, 0.000148240),
    _03050500(3, 5, 5, 0, 0.000748130),
    _13000000(13, 0, 0, 0, 0.00000),
    _03020503(3, 2, 5, 3, 0.0129492),
    _03020206(3, 2, 2, 6, 0.00470896),
    _00050701(0, 5, 7, 1, 4.52700e-05),
    _02030305(2, 3, 3, 5, 0.0129387),
    _02010901(2, 1, 9, 1, 1.48400e-05),
    _09000103(9, 0, 1, 3, 4.21000e-06),
    _02050402(2, 5, 4, 2, 0.00881117),
    _04040302(4, 4, 3, 2, 0.0179720),
    _01000606(1, 0, 6, 6, 5.99300e-05),
    _08030200(8, 3, 2, 0, 4.46500e-05),
    _04050301(4, 5, 3, 1, 0.00539130),
    _03030700(3, 3, 7, 0, 0.000219500),
    _03000901(3, 0, 9, 1, 4.17000e-06),
    _00050206(0, 5, 2, 6, 0.000274500),
    _03010405(3, 1, 4, 5, 0.00539653),
    _05000107(5, 0, 1, 7, 4.48500e-05),
    _04020106(4, 2, 1, 6, 0.00195527),
    _03020008(3, 2, 0, 8, 4.48100e-05),
    _00040504(0, 4, 5, 4, 0.00103317),
    _01030405(1, 3, 4, 5, 0.00538591),
    _04010107(4, 1, 1, 7, 0.000327470),
    _02000407(2, 0, 4, 7, 0.000148900),
    _03060202(3, 6, 2, 2, 0.00471089),
    _06040003(6, 4, 0, 3, 0.000553160),
    _06000601(6, 0, 6, 1, 6.02400e-05),
    _04070200(4, 7, 2, 0, 0.000152110),
    _01070005(1, 7, 0, 5, 4.58000e-05),
    _00000607(0, 0, 6, 7, 4.84000e-06),
    _02001001(2, 0, 10, 1, 4.40000e-07),
    _01100002(1, 10, 0, 2, 5.60000e-07),
    _03000604(3, 0, 6, 4, 0.000552950),
    _04000504(4, 0, 5, 4, 0.00103230),
    _06060100(6, 6, 1, 0, 6.04500e-05),
    _05020006(5, 2, 0, 6, 0.000271140),
    _00100102(0, 10, 1, 2, 4.70000e-07),
    _04020403(4, 2, 4, 3, 0.0179477),
    _05020204(5, 2, 2, 4, 0.00880826),
    _02070202(2, 7, 2, 2, 0.00127688),
    _01050403(1, 5, 4, 3, 0.00537557),
    _00110002(0, 11, 0, 2, 1.00000e-08),
    _08030101(8, 3, 1, 1, 9.63600e-05),
    _05010601(5, 1, 6, 1, 0.000588560),
    _05020105(5, 2, 1, 5, 0.00264927),
    _02090002(2, 9, 0, 2, 6.89000e-06),
    _02090200(2, 9, 2, 0, 6.63000e-06),
    _02060500(2, 6, 5, 0, 0.000271220),
    _03010801(3, 1, 8, 1, 9.79500e-05),
    _05030500(5, 3, 5, 0, 0.000749870),
    _01060303(1, 6, 3, 3, 0.00287817),
    _01090300(1, 9, 3, 0, 4.39000e-06),
    _10020100(10, 2, 1, 0, 3.90000e-07),
    _00001003(0, 0, 10, 3, 1.70000e-07),
    _03080101(3, 8, 1, 1, 9.76500e-05),
    _07000006(7, 0, 0, 6, 4.99000e-06),
    _02000506(2, 0, 5, 6, 0.000269840),
    _00060007(0, 6, 0, 7, 4.45000e-06),
    _04040005(4, 4, 0, 5, 0.00103628),
    _07000501(7, 0, 5, 1, 4.47100e-05),
    _00070402(0, 7, 4, 2, 0.000151160),
    _01030207(1, 3, 2, 7, 0.000782050),
    _09040000(9, 4, 0, 0, 8.60000e-07),
    _01000804(1, 0, 8, 4, 1.85700e-05),
    _01090003(1, 9, 0, 3, 4.16000e-06),
    _08000302(8, 0, 3, 2, 4.48700e-05),
    _00000904(0, 0, 9, 4, 9.20000e-07),
    _02070004(2, 7, 0, 4, 0.000149370),
    _04030105(4, 3, 1, 5, 0.00538065),
    _02030008(2, 3, 0, 8, 4.44000e-05),
    _01040404(1, 4, 4, 4, 0.00748569),
    _06000106(6, 0, 1, 6, 6.01900e-05),
    _07040002(7, 4, 0, 2, 0.000153450),
    _07000105(7, 0, 1, 5, 4.40400e-05),
    _00080005(0, 8, 0, 5, 2.78000e-06),
    _06000403(6, 0, 4, 3, 0.000549530),
    _00100300(0, 10, 3, 0, 1.60000e-07),
    _04030006(4, 3, 0, 6, 0.000553470),
    _03000505(3, 0, 5, 5, 0.000745350),
    _03030601(3, 3, 6, 1, 0.00287604),
    _02080003(2, 8, 0, 3, 4.43400e-05),
    _05040400(5, 4, 4, 0, 0.00103463),
    _03000802(3, 0, 8, 2, 4.62000e-05),
    _01040800(1, 4, 8, 0, 1.90100e-05),
    _00030505(0, 3, 5, 5, 0.000744510),
    _01080004(1, 8, 0, 4, 1.89600e-05),
    _03020305(3, 2, 3, 5, 0.0129428),
    _05050201(5, 5, 2, 1, 0.00263928),
    _01050601(1, 5, 6, 1, 0.000586610),
    _00050800(0, 5, 8, 0, 2.52000e-06),
    _06050200(6, 5, 2, 0, 0.000273190),
    _06030004(6, 3, 0, 4, 0.000550240),
    _04030600(4, 3, 6, 0, 0.000554630),
    _01010209(1, 1, 2, 9, 1.55700e-05),
    _02100001(2, 10, 0, 1, 5.40000e-07),
    _03001000(3, 0, 10, 0, 1.70000e-07),
    _04050004(4, 5, 0, 4, 0.00103319),
    _04000603(4, 0, 6, 3, 0.000553960),
    _01040008(1, 4, 0, 8, 1.77900e-05),
    _06050002(6, 5, 0, 2, 0.000270970),
    _03080200(3, 8, 2, 0, 4.48600e-05),
    _01020604(1, 2, 6, 4, 0.00195715),
    _06020401(6, 2, 4, 1, 0.00196005),
    _08010004(8, 1, 0, 4, 1.82600e-05),
    _01060402(1, 6, 4, 2, 0.00196119),
    _07000402(7, 0, 4, 2, 0.000150250),
    _01090201(1, 9, 2, 1, 1.46300e-05),
    _01011100(1, 1, 11, 0, 2.00000e-08),
    _04090000(4, 9, 0, 0, 7.20000e-07),
    _08020003(8, 2, 0, 3, 4.51600e-05),
    _00040405(0, 4, 4, 5, 0.00103606),
    _03020602(3, 2, 6, 2, 0.00469914),
    _00090103(0, 9, 1, 3, 4.35000e-06),
    _04000306(4, 0, 3, 6, 0.000556420),
    _05010205(5, 1, 2, 5, 0.00264624),
    _06000205(6, 0, 2, 5, 0.000271290),
    _01060501(1, 6, 5, 1, 0.000586120),
    _02010505(2, 1, 5, 5, 0.00264826),
    _04020601(4, 2, 6, 1, 0.00195606),
    _00090202(0, 9, 2, 2, 6.66000e-06),
    _06020005(6, 2, 0, 5, 0.000271100),
    _04000900(4, 0, 9, 0, 8.10000e-07),
    _02000011(2, 0, 0, 11, 2.00000e-08),
    _04010305(4, 1, 3, 5, 0.00539230),
    _03060004(3, 6, 0, 4, 0.000550650),
    _07060000(7, 6, 0, 0, 4.49000e-06),
    _00060403(0, 6, 4, 3, 0.000552530),
    _05000008(5, 0, 0, 8, 2.36000e-06),
    _00080302(0, 8, 3, 2, 4.49800e-05),
    _00060601(0, 6, 6, 1, 6.01000e-05),
    _02020702(2, 2, 7, 2, 0.00128510),
    _08010202(8, 1, 2, 2, 0.000158720),
    _06030400(6, 3, 4, 0, 0.000552990),
    _02011000(2, 1, 10, 0, 5.00000e-07),
    _00001201(0, 0, 12, 1, 0.00000),
    _01120000(1, 12, 0, 0, 0.00000),
    _05010403(5, 1, 4, 3, 0.00539809),
    _02040502(2, 4, 5, 2, 0.00881682),
    _00010408(0, 1, 4, 8, 1.88600e-05),
    _03010009(3, 1, 0, 9, 4.03000e-06),
    _08000203(8, 0, 2, 3, 4.49000e-05),
    _02070103(2, 7, 1, 3, 0.000782390),
    _02030404(2, 3, 4, 4, 0.0179476),
    _00010111(0, 1, 1, 11, 5.00000e-08),
    _00000508(0, 0, 5, 8, 2.57000e-06),
    _00000310(0, 0, 3, 10, 1.20000e-07),
    _03100000(3, 10, 0, 0, 7.00000e-08),
    _09000400(9, 0, 4, 0, 8.40000e-07),
    _10000003(10, 0, 0, 3, 1.20000e-07),
    _08030002(8, 3, 0, 2, 4.41500e-05),
    _04080001(4, 8, 0, 1, 1.85900e-05),
    _00050404(0, 5, 4, 4, 0.00103869),
    _05020600(5, 2, 6, 0, 0.000269910),
    _00040009(0, 4, 0, 9, 6.50000e-07),
    _05070100(5, 7, 1, 0, 4.57200e-05),
    _05070001(5, 7, 0, 1, 4.44900e-05),
    _03040402(3, 4, 4, 2, 0.0179705),
    _00090301(0, 9, 3, 1, 4.12000e-06),
    _05030302(5, 3, 3, 2, 0.0129420),
    _00070501(0, 7, 5, 1, 4.61600e-05),
    _05050300(5, 5, 3, 0, 0.000745100),
    _06010105(6, 1, 1, 5, 0.000591760),
    _06010303(6, 1, 3, 3, 0.00287643),
    _04060003(4, 6, 0, 3, 0.000553870),
    _01001101(1, 0, 11, 1, 3.00000e-08),
    _00060304(0, 6, 3, 4, 0.000553640),
    _02020603(2, 2, 6, 3, 0.00470592),
    _03010702(3, 1, 7, 2, 0.000783140),
    _04050400(4, 5, 4, 0, 0.00103716),
    _03010900(3, 1, 9, 0, 4.25000e-06),
    _07000204(7, 0, 2, 4, 0.000150980),
    _09000004(9, 0, 0, 4, 7.70000e-07),
    _01050007(1, 5, 0, 7, 4.40300e-05),
    _10010101(10, 1, 1, 1, 9.90000e-07),
    _01030009(1, 3, 0, 9, 4.12000e-06),
    _00021100(0, 2, 11, 0, 1.00000e-08),
    _03030007(3, 3, 0, 7, 0.000220000),
    _02000209(2, 0, 2, 9, 7.09000e-06),
    _09010300(9, 1, 3, 0, 3.79000e-06),
    _01020307(1, 2, 3, 7, 0.000786990),
    _03000703(3, 0, 7, 3, 0.000222290),
    _03020800(3, 2, 8, 0, 4.43600e-05),
    _03020701(3, 2, 7, 1, 0.000778990),
    _00000211(0, 0, 2, 11, 0.00000),
    _01020010(1, 2, 0, 10, 4.60000e-07),
    _02030800(2, 3, 8, 0, 4.50400e-05),
    _02020405(2, 2, 4, 5, 0.00881013),
    _04000405(4, 0, 4, 5, 0.00103896),
    _10030000(10, 3, 0, 0, 1.00000e-07),
    _06030202(6, 3, 2, 2, 0.00468803),
    _05060002(5, 6, 0, 2, 0.000272010),
    _00070105(0, 7, 1, 5, 4.40600e-05),
    _01040503(1, 4, 5, 3, 0.00537885),
    _03000010(3, 0, 0, 10, 1.10000e-07),
    _03010306(3, 1, 3, 6, 0.00287055),
    _02040403(2, 4, 4, 3, 0.0179661),
    _00060205(0, 6, 2, 5, 0.000271040),
    _00010210(0, 1, 2, 10, 4.30000e-07),
    _01050205(1, 5, 2, 5, 0.00264229),
    _01060204(1, 6, 2, 4, 0.00195646),
    _05020501(5, 2, 5, 1, 0.00264132),
    _00030901(0, 3, 9, 1, 4.08000e-06),
    _03000208(3, 0, 2, 8, 4.53700e-05),
    _06050101(6, 5, 1, 1, 0.000591010),
    _03060103(3, 6, 1, 3, 0.00286860),
    _03040501(3, 4, 5, 1, 0.00538892),
    _00070006(0, 7, 0, 6, 4.66000e-06),
    _08040001(8, 4, 0, 1, 1.85500e-05),
    _01060105(1, 6, 1, 5, 0.000585420),
    _02060401(2, 6, 4, 1, 0.00196336),
    _01000210(1, 0, 2, 10, 5.20000e-07),
    _07010401(7, 1, 4, 1, 0.000326380),
    _02000704(2, 0, 7, 4, 0.000151620),
    _08020102(8, 2, 1, 2, 0.000160510),
    _01001200(1, 0, 12, 0, 0.00000),
    _03010504(3, 1, 5, 4, 0.00539879),
    _07030003(7, 3, 0, 3, 0.000219180),
    _07030300(7, 3, 3, 0, 0.000222780),
    _10010002(10, 1, 0, 2, 5.40000e-07),
    _02010010(2, 1, 0, 10, 4.30000e-07),
    _01050502(1, 5, 5, 2, 0.00263936),
    _01020703(1, 2, 7, 3, 0.000782170),
    _08000104(8, 0, 1, 4, 1.87900e-05),
    _01100200(1, 10, 2, 0, 3.90000e-07),
    _00030406(0, 3, 4, 6, 0.000554110),
    _01110001(1, 11, 0, 1, 3.00000e-08),
    _01030504(1, 3, 5, 4, 0.00537866),
    _04070101(4, 7, 1, 1, 0.000324870),
    _00120001(0, 12, 0, 1, 0.00000),
    _00110101(0, 11, 1, 1, 1.00000e-08),
    _01010803(1, 1, 8, 3, 9.83500e-05),
    _00030208(0, 3, 2, 8, 4.43300e-05),
    _04020205(4, 2, 2, 5, 0.00880427),
    _04000207(4, 0, 2, 7, 0.000149410),
    _00020110(0, 2, 1, 10, 4.00000e-07),
    _03030502(3, 3, 5, 2, 0.0129270),
    _00020704(0, 2, 7, 4, 0.000149790),
    _05030104(5, 3, 1, 4, 0.00539386),
    _08000005(8, 0, 0, 5, 2.34000e-06),
    _01040602(1, 4, 6, 2, 0.00196315),
    _01000903(1, 0, 9, 3, 4.11000e-06),
    _02010703(2, 1, 7, 3, 0.000783460),
    _00050305(0, 5, 3, 5, 0.000741080),
    _05000800(5, 0, 8, 0, 2.29000e-06),
    _05000602(5, 0, 6, 2, 0.000272400),
    _04010206(4, 1, 2, 6, 0.00196126),
    _04050202(4, 5, 2, 2, 0.00882427),
    _06000502(6, 0, 5, 2, 0.000270530),
    _02020108(2, 2, 1, 8, 0.000159250),
    _03010207(3, 1, 2, 7, 0.000787670),
    _03040105(3, 4, 1, 5, 0.00540171),
    _04020304(4, 2, 3, 4, 0.0179613),
    _00100201(0, 10, 2, 1, 4.80000e-07),
    _01080103(1, 8, 1, 3, 9.79100e-05),
    _11000101(11, 0, 1, 1, 2.00000e-08),
    _11000200(11, 0, 2, 0, 1.00000e-08),
    _00110200(0, 11, 2, 0, 1.00000e-08),
    _02050204(2, 5, 2, 4, 0.00882239),
    _03030304(3, 3, 3, 4, 0.0263321),
    _02010109(2, 1, 1, 9, 1.51600e-05),
    _00000409(0, 0, 4, 9, 7.40000e-07),
    _04000801(4, 0, 8, 1, 1.78000e-05),
    _07010005(7, 1, 0, 5, 4.53700e-05),
    _09010102(9, 1, 1, 2, 1.49400e-05),
    _02030107(2, 3, 1, 7, 0.000781540),
    _06020104(6, 2, 1, 4, 0.00195779),
    _05010304(5, 1, 3, 4, 0.00537617),
    _01080301(1, 8, 3, 1, 9.61800e-05),
    _06010501(6, 1, 5, 1, 0.000590310),
    _01080202(1, 8, 2, 2, 0.000159950),
    _02060203(2, 6, 2, 3, 0.00471087),
    _00010012(0, 1, 0, 12, 0.00000),
    _01010407(1, 1, 4, 7, 0.000327720),
    _01040701(1, 4, 7, 1, 0.000325640),
    _07020301(7, 2, 3, 1, 0.000781820),
    _00020803(0, 2, 8, 3, 4.54800e-05),
    _07030201(7, 3, 2, 1, 0.000783290),
    _08000401(8, 0, 4, 1, 1.89800e-05),
    _00020209(0, 2, 2, 9, 6.73000e-06),
    _01030801(1, 3, 8, 1, 9.89800e-05),
    _06010204(6, 1, 2, 4, 0.00196052),
    _01010506(1, 1, 5, 6, 0.000588460),
    _04020502(4, 2, 5, 2, 0.00882013),
    _02020207(2, 2, 2, 7, 0.00128085),
    _06030301(6, 3, 3, 1, 0.00287121),
    _08010400(8, 1, 4, 0, 1.94300e-05),
    _00000805(0, 0, 8, 5, 2.76000e-06),
    _05040301(5, 4, 3, 1, 0.00539295),
    _02050105(2, 5, 1, 5, 0.00264988),
    _00080203(0, 8, 2, 3, 4.56600e-05),
    _00010606(0, 1, 6, 6, 6.00100e-05),
    _12000100(12, 0, 1, 0, 0.00000),
    _00040603(0, 4, 6, 3, 0.000552830),
    _04010800(4, 1, 8, 0, 1.94900e-05),
    _07020004(7, 2, 0, 4, 0.000150820),
    _00040900(0, 4, 9, 0, 8.80000e-07),
    _04010008(4, 1, 0, 8, 1.85100e-05),
    _00080401(0, 8, 4, 1, 1.83800e-05),
    _01020505(1, 2, 5, 5, 0.00264176),
    _06020302(6, 2, 3, 2, 0.00470831),
    _07020103(7, 2, 1, 3, 0.000785470),
    _07000303(7, 0, 3, 3, 0.000222380),
    _02030701(2, 3, 7, 1, 0.000783160),
    _00000706(0, 0, 7, 6, 4.84000e-06),
    _05040103(5, 4, 1, 3, 0.00538856),
    _03000307(3, 0, 3, 7, 0.000218860),
    _12000001(12, 0, 0, 1, 0.00000),
    _04040401(4, 4, 4, 1, 0.00747906),
    _10000102(10, 0, 1, 2, 6.00000e-07),
    _00011101(0, 1, 11, 1, 1.00000e-08),
    _01020109(1, 2, 1, 9, 1.41200e-05),
    _03010603(3, 1, 6, 3, 0.00287842),
    _01010605(1, 1, 6, 5, 0.000586680),
    _05030203(5, 3, 2, 3, 0.0129300),
    _01021000(1, 2, 10, 0, 4.60000e-07),
    _01010902(1, 1, 9, 2, 1.50600e-05),
    _06020203(6, 2, 2, 3, 0.00469775),
    _09010201(9, 1, 2, 1, 1.45800e-05),
    _03040600(3, 4, 6, 0, 0.000553490),
    _05040004(5, 4, 0, 4, 0.00103055),
    _06030103(6, 3, 1, 3, 0.00287253),
    _01060600(1, 6, 6, 0, 6.21300e-05),
    _05000503(5, 0, 5, 3, 0.000748140),
    _01001002(1, 0, 10, 2, 4.80000e-07),
    _11010100(11, 1, 1, 0, 2.00000e-08),
    _02000902(2, 0, 9, 2, 6.81000e-06),
    _04050103(4, 5, 1, 3, 0.00539073),
    _07010500(7, 1, 5, 0, 4.58000e-05),
    _04060102(4, 6, 1, 2, 0.00195788),
    _01011001(1, 1, 10, 1, 8.70000e-07),
    _02040205(2, 4, 2, 5, 0.00881029),
    _01010011(1, 1, 0, 11, 3.00000e-08),
    _01030900(1, 3, 9, 0, 4.21000e-06),
    _02020801(2, 2, 8, 1, 0.000160910),
    _03070102(3, 7, 1, 2, 0.000785320),
    _08050000(8, 5, 0, 0, 2.82000e-06),
    _07010203(7, 1, 2, 3, 0.000785330),
    _12010000(12, 1, 0, 0, 0.00000),
    _09000202(9, 0, 2, 2, 6.77000e-06),
    _11010001(11, 1, 0, 1, 1.00000e-08),
    _01050700(1, 5, 7, 0, 4.48000e-05),
    _01020208(1, 2, 2, 8, 0.000160500),
    _07040200(7, 4, 2, 0, 0.000150240),
    _02000803(2, 0, 8, 3, 4.52500e-05),
    _03050005(3, 5, 0, 5, 0.000743630),
    _03050401(3, 5, 4, 1, 0.00537635),
    _02030206(2, 3, 2, 6, 0.00469601),
    _03090001(3, 9, 0, 1, 4.21000e-06),
    _10000300(10, 0, 3, 0, 1.60000e-07),
    _00020011(0, 2, 0, 11, 0.00000),
    _00100003(0, 10, 0, 3, 1.60000e-07),
    _01010110(1, 1, 1, 10, 8.70000e-07),
    _04040104(4, 4, 1, 4, 0.00747745),
    _04080100(4, 8, 1, 0, 1.80400e-05),
    _04060300(4, 6, 3, 0, 0.000549790),
    _10020001(10, 2, 0, 1, 4.10000e-07),
    _01040206(1, 4, 2, 6, 0.00195486),
    _05000305(5, 0, 3, 5, 0.000744850),
    _06040201(6, 4, 2, 1, 0.00194470),
    _05000701(5, 0, 7, 1, 4.58800e-05),
    _00050008(0, 5, 0, 8, 2.43000e-06),
    _06060001(6, 6, 0, 1, 6.10700e-05),
    _02020900(2, 2, 9, 0, 6.91000e-06),
    _01100101(1, 10, 1, 1, 9.30000e-07),
    _00020902(0, 2, 9, 2, 6.72000e-06),
    _06020500(6, 2, 5, 0, 0.000272190),
    _01070302(1, 7, 3, 2, 0.000781600),
    _01030306(1, 3, 3, 6, 0.00286478),
    _03000109(3, 0, 1, 9, 4.08000e-06),
    _02040106(2, 4, 1, 6, 0.00196170),
    _02030602(2, 3, 6, 2, 0.00469452),
    _05040202(5, 4, 2, 2, 0.00880641),
    _00030802(0, 3, 8, 2, 4.49200e-05),
    _05060101(5, 6, 1, 1, 0.000590180),
    _01020901(1, 2, 9, 1, 1.48600e-05),
    _03070201(3, 7, 2, 1, 0.000787270),
    _00040702(0, 4, 7, 2, 0.000150620),
    _01050106(1, 5, 1, 6, 0.000592520),
    _04040500(4, 4, 5, 0, 0.00103372),
    _08020300(8, 2, 3, 0, 4.45900e-05),
    _00040306(0, 4, 3, 6, 0.000551460),
    _01050304(1, 5, 3, 4, 0.00538517),
    _02001100(2, 0, 11, 0, 2.00000e-08),
    _04030303(4, 3, 3, 3, 0.0263464),
    _08020201(8, 2, 2, 1, 0.000159580),
    _03080002(3, 8, 0, 2, 4.53400e-05),
    _04000009(4, 0, 0, 9, 7.90000e-07),
    _10010200(10, 1, 2, 0, 4.70000e-07),
    _08040100(8, 4, 1, 0, 1.86600e-05),
    _02010406(2, 1, 4, 6, 0.00195816),
    _07010302(7, 1, 3, 2, 0.000781130),
    _05050003(5, 5, 0, 3, 0.000748160),
    _04010404(4, 1, 4, 4, 0.00747628),
    _00030307(0, 3, 3, 7, 0.000222430),
    _00031000(0, 3, 10, 0, 1.20000e-07),
    _05020402(5, 2, 4, 2, 0.00881520),
    _03000406(3, 0, 4, 6, 0.000553440),
    _03050302(3, 5, 3, 2, 0.0129367),
    _01060006(1, 6, 0, 6, 5.97800e-05);

    private final int[] num;
    private final double probability;

    /**
     * 
     * @param suit
     *            The suit
     * @return The number of cards in the given suit.
     */
    public int numInSuit(int suit) {
        return num[suit];
    }

    private Shape(int c, int d, int h, int s, double probability) {
        num = new int[] { c, d, h, s };
        this.probability = probability;
    }

    private static final Shape[][][] numToShape = new Shape[14][14][14];

    static {
        for (Shape s : Shape.values()) {
            numToShape[s.numInSuit(0)][s.numInSuit(1)][s.numInSuit(2)] = s;
        }
    }

    /**
     * 
     * @param num
     *            The array of suit lengths
     * @return The shape corresponding to the numbers;
     */
    public static Shape getShape(int... num) {
        if (num.length != 4) {
            throw new IllegalArgumentException("Less than 4 suits specified");
        }
        int n = num[0] + num[1] + num[2] + num[3];
        if (n != 13) {
            throw new IllegalArgumentException("Not enough cards");
        }
        if (num[0] < 0 || num[1] < 0 || num[2] < 0 || num[3] < 0) {
            throw new IllegalArgumentException("Negative number in a suit");
        }
        return numToShape[num[0]][num[1]][num[2]];
    }

    /**
     * @return true if the shape is balance
     */
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

    /**
     * @return true if the shape is 4333
     */
    public boolean isSuperBalanced() {
        for (int suit = 0; suit < 4; suit++) {
            int len = numInSuit(suit);
            if (len < 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param suit
     *            The suit
     * @param rng
     *            The range
     * @return True if number of cards in the given suit matches the range.
     */
    public boolean isSuitInRange(int suit, SuitLengthRange rng) {
        return rng.contains(numInSuit(suit));
    }

    /**
     * 
     * @param suit
     *            The suit
     * @param among
     *            Bit pattern of suits
     * @return true if the given suit is longer or equal among all other suits in the bit pattern
     */
    public boolean isLongerOrEqual(int suit, short among) {
        int len = numInSuit(suit);
        return !BitUtil.stream(among).filter(s -> numInSuit(s) > len).findFirst().isPresent();
    }
    
    /**
     * 
     * @return The probability of this shape
     */
    public double getProbability() {
        return probability;
    }
}
