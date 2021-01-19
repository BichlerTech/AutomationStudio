/* ========================================================================
 * Copyright (c) 2005-2015 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================*/

package org.opcfoundation.ua.core;

import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class StandardEngineeringUnits {

	public static final String URI = "https://opcfoundation.org/UA/units/un/cefact";

	private static Map<Integer, EUInformation> unitMap = new HashMap<Integer, EUInformation>();
	private static Map<String, EUInformation> ccUnitMap = new HashMap<String, EUInformation>();
	private static List<EUInformation> all = new ArrayList<EUInformation>();

	public static final EUInformation ACCESS_LINE = init("AL", 16716, "", "access line");
	public static final EUInformation ACCOUNTING_UNIT = init("E50", 4535600, "", "accounting unit");
	public static final EUInformation ACRE = init("ACR", 4277074, "acre", "acre");
	public static final EUInformation ACRE_FOOT_BASED_ON_US_SURVEY_FOOT = init("M67", 5060151, "acre-ft (US survey)",
			"acre-foot (based on U.S. survey foot)");
	public static final EUInformation ACTIVE_UNIT = init("E25", 4534837, "", "active unit");
	public static final EUInformation ACTIVITY = init("ACT", 4277076, "", "activity");
	public static final EUInformation ACTUAL_PER_360 = init("M37", 5059383, "y (360 days)", "actual/360");
	public static final EUInformation ADDITIONAL_MINUTE = init("AH", 16712, "", "additional minute");
	public static final EUInformation AIR_DRY_METRIC_TON = init("MD", 19780, "", "air dry metric ton");
	public static final EUInformation AIR_DRY_TON = init("E28", 4534840, "", "air dry ton");
	public static final EUInformation ALCOHOLIC_STRENGTH_BY_MASS = init("ASM", 4281165, "",
			"alcoholic strength by mass");
	public static final EUInformation ALCOHOLIC_STRENGTH_BY_VOLUME = init("ASU", 4281173, "",
			"alcoholic strength by volume");
	public static final EUInformation AMERICAN_WIRE_GAUGE = init("AWG", 4282183, "AWG", "american wire gauge");
	public static final EUInformation AMPERE = init("AMP", 4279632, "A", "ampere");
	public static final EUInformation AMPERE_HOUR = init("AMH", 4279624, "A\u00B7h", "ampere hour");
	public static final EUInformation AMPERE_MINUTE = init("N95", 5126453, "A\u00B7min", "ampere minute");
	public static final EUInformation AMPERE_PER_CENTIMETRE = init("A2", 16690, "A/cm", "ampere per centimetre");
	public static final EUInformation AMPERE_PER_KILOGRAM = init("H31", 4731697, "A/kg", "ampere per kilogram");
	public static final EUInformation AMPERE_PER_METRE = init("AE", 16709, "A/m", "ampere per metre");
	public static final EUInformation AMPERE_PER_MILLIMETRE = init("A3", 16691, "A/mm", "ampere per millimetre");
	public static final EUInformation AMPERE_PER_PASCAL = init("N93", 5126451, "A/Pa", "ampere per pascal");
	public static final EUInformation AMPERE_PER_SQUARE_CENTIMETRE = init("A4", 16692, "A/cm\u00B2",
			"ampere per square centimetre");
	public static final EUInformation AMPERE_PER_SQUARE_METRE = init("A41", 4273201, "A/m\u00B2",
			"ampere per square metre");
	public static final EUInformation AMPERE_PER_SQUARE_METRE_KELVIN_SQUARED = init("A6", 16694,
			"A/(m\u00B2\u00B7K\u00B2)", "ampere per square metre kelvin squared");
	public static final EUInformation AMPERE_PER_SQUARE_MILLIMETRE = init("A7", 16695, "A/mm\u00B2",
			"ampere per square millimetre");
	public static final EUInformation AMPERE_SECOND = init("A8", 16696, "A\u00B7s", "ampere second");
	public static final EUInformation AMPERE_SQUARED_SECOND = init("H32", 4731698, "A\u00B2\u00B7s",
			"ampere squared second");
	public static final EUInformation AMPERE_SQUARE_METRE = init("A5", 16693, "A\u00B7m\u00B2", "ampere square metre");
	public static final EUInformation AMPERE_SQUARE_METRE_PER_JOULE_SECOND = init("A10", 4272432,
			"A\u00B7m\u00B2/(J\u00B7s)", "ampere square metre per joule second");
	public static final EUInformation ANGSTROM = init("A11", 4272433, "\u00C5", "angstrom");
	public static final EUInformation ANTI_HEMOPHILIC_FACTOR_AHF_UNIT = init("AQ", 16721, "",
			"anti-hemophilic factor (AHF) unit");
	public static final EUInformation ASSEMBLY = init("AY", 16729, "", "assembly");
	public static final EUInformation ASSORTMENT = init("AS", 16723, "", "assortment");
	public static final EUInformation ASTRONOMICAL_UNIT = init("A12", 4272434, "ua", "astronomical unit");
	public static final EUInformation ATTOFARAD = init("H48", 4731960, "aF", "attofarad");
	public static final EUInformation ATTOJOULE = init("A13", 4272435, "aJ", "attojoule");
	public static final EUInformation AVERAGE_MINUTE_PER_CALL = init("AI", 16713, "", "average minute per call");
	public static final EUInformation BALL = init("AA", 16705, "", "ball");
	public static final EUInformation BARN = init("A14", 4272436, "b", "barn");
	public static final EUInformation BARN_PER_ELECTRONVOLT = init("A15", 4272437, "b/eV", "barn per electronvolt");
	public static final EUInformation BARN_PER_STERADIAN = init("A17", 4272439, "b/sr", "barn per steradian");
	public static final EUInformation BARN_PER_STERADIAN_ELECTRONVOLT = init("A16", 4272438, "b/(sr\u00B7eV)",
			"barn per steradian electronvolt");
	public static final EUInformation BARREL_IMPERIAL = init("B4", 16948, "", "barrel, imperial");
	public static final EUInformation BARREL_UK_PETROLEUM = init("J57", 4863287, "bbl (UK liq.)",
			"barrel (UK petroleum)");
	public static final EUInformation BARREL_UK_PETROLEUM_PER_DAY = init("J59", 4863289, "bbl (UK liq.)/d",
			"barrel (UK petroleum) per day");
	public static final EUInformation BARREL_UK_PETROLEUM_PER_HOUR = init("J60", 4863536, "bbl (UK liq.)/h",
			"barrel (UK petroleum) per hour");
	public static final EUInformation BARREL_UK_PETROLEUM_PER_MINUTE = init("J58", 4863288, "bbl (UK liq.)/min",
			"barrel (UK petroleum) per minute");
	public static final EUInformation BARREL_UK_PETROLEUM_PER_SECOND = init("J61", 4863537, "bbl (UK liq.)/s",
			"barrel (UK petroleum) per second");
	public static final EUInformation BARREL_US = init("BLL", 4344908, "barrel (US)", "barrel (US)");
	public static final EUInformation BARREL_US_PER_DAY = init("B1", 16945, "barrel\u00A0(US)/d",
			"barrel (US) per day");
	public static final EUInformation BARREL_US_PER_MINUTE = init("5A", 13633, "barrel (US)/min",
			"barrel (US) per minute");
	public static final EUInformation BARREL_US_PETROLEUM_PER_HOUR = init("J62", 4863538, "bbl (US)/h",
			"barrel (US petroleum) per hour");
	public static final EUInformation BARREL_US_PETROLEUM_PER_SECOND = init("J63", 4863539, "bbl (US)/s",
			"barrel (US petroleum) per second");
	public static final EUInformation BAR_CUBIC_METRE_PER_SECOND = init("F92", 4602162, "bar\u00B7m\u00B3/s",
			"bar cubic metre per second");
	public static final EUInformation BAR_LITRE_PER_SECOND = init("F91", 4602161, "bar\u00B7l/s",
			"bar litre per second");
	public static final EUInformation BAR_PER_BAR = init("J56", 4863286, "bar/bar", "bar per bar");
	public static final EUInformation BAR_PER_KELVIN = init("F81", 4601905, "bar/K", "bar per kelvin");
	public static final EUInformation BAR_UNIT_OF_PRESSURE = init("BAR", 4342098, "bar", "bar [unit of pressure]");
	public static final EUInformation BASE_BOX = init("BB", 16962, "", "base box");
	public static final EUInformation BATCH = init("5B", 13634, "", "batch");
	public static final EUInformation BATTING_POUND = init("B3", 16947, "", "batting pound");
	public static final EUInformation BAUD = init("J38", 4862776, "Bd", "baud");
	public static final EUInformation BEATS_PER_MINUTE = init("BPM", 4345933, "BPM", "beats per minute");
	public static final EUInformation BEAUFORT = init("M19", 5058873, "Bft", "Beaufort");
	public static final EUInformation BECQUEREL = init("BQL", 4346188, "Bq", "becquerel");
	public static final EUInformation BECQUEREL_PER_CUBIC_METRE = init("A19", 4272441, "Bq/m\u00B3",
			"becquerel per cubic metre");
	public static final EUInformation BECQUEREL_PER_KILOGRAM = init("A18", 4272440, "Bq/kg", "becquerel per kilogram");
	public static final EUInformation BEL = init("M72", 5060402, "B", "bel");
	public static final EUInformation BEL_PER_METRE = init("P43", 5256243, "B/m", "bel per metre");
	public static final EUInformation BIG_POINT = init("H82", 4732978, "bp", "big point");
	public static final EUInformation BILLION_EUR = init("BIL", 4344140, "", "billion (EUR)");
	public static final EUInformation BIOT = init("N96", 5126454, "Bi", "biot");
	public static final EUInformation BIT = init("A99", 4274489, "bit", "bit");
	public static final EUInformation BIT_PER_CUBIC_METRE = init("F01", 4599857, "bit/m\u00B3", "bit per cubic metre");
	public static final EUInformation BIT_PER_METRE = init("E88", 4536376, "bit/m", "bit per metre");
	public static final EUInformation BIT_PER_SECOND = init("B10", 4337968, "bit/s", "bit per second");
	public static final EUInformation BIT_PER_SQUARE_METRE = init("E89", 4536377, "bit/m\u00B2",
			"bit per square metre");
	public static final EUInformation BLANK = init("H21", 4731441, "", "blank");
	public static final EUInformation BOARD_FOOT = init("BFT", 4343380, "fbm", "board foot");
	public static final EUInformation BOOK = init("D63", 4470323, "", "book");
	public static final EUInformation BRAKE_HORSE_POWER = init("BHP", 4343888, "BHP", "brake horse power");
	public static final EUInformation BRITISH_THERMAL_UNIT_39__FAHRENHEIT_ = init("N66", 5125686, "Btu (39 \u00BAF)",
			"British thermal unit (39 \u00BAF)");
	public static final EUInformation BRITISH_THERMAL_UNIT_59__FAHRENHEIT_ = init("N67", 5125687, "Btu (59 \u00BAF)",
			"British thermal unit (59 \u00BAF)");
	public static final EUInformation BRITISH_THERMAL_UNIT_60__FAHRENHEIT_ = init("N68", 5125688, "Btu (60 \u00BAF)",
			"British thermal unit (60 \u00BAF)");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE = init("BTU", 4346965, "BtuIT",
			"British thermal unit (international table)");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_FOOT_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J40", 4863024, "BtuIT\u00B7ft/(h\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (international table) foot per hour\u00A0square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_INCH_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J41", 4863025, "BtuIT\u00B7in/(h\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (international table) inch per hour square\u00A0foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_INCH_PER_SECOND_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J42", 4863026, "BtuIT\u00B7in/(s\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (international table) inch per second square\u00A0foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_CUBIC_FOOT = init("N58", 5125432,
			"BtuIT/ft\u00B3", "British thermal unit (international table) per cubic foot");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_DEGREE_FAHRENHEIT = init("N60",
			5125680, "BtuIT/\u00BAF", "British thermal unit (international table) per degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_DEGREE_RANKINE = init("N62", 5125682,
			"BtuIT/\u00BAR", "British thermal unit (international table) per degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_HOUR = init("2I", 12873, "BtuIT/h",
			"British thermal unit (international table) per hour");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"N74", 5125940, "BtuIT/(h\u00B7ft\u00B2\u00B7\u00BAF)",
			"British thermal unit (international table) per hour square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_HOUR_SQUARE_FOOT_DEGREE_RANKINE = init(
			"A23", 4272691, "BtuIT/(h\u00B7ft\u00B2\u00B7\u00B0R)",
			"British thermal unit (international table) per hour square foot degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_MINUTE = init("J44", 4863028,
			"BtuIT/min", "British thermal unit (international table) per minute");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_POUND = init("AZ", 16730, "BtuIT/lb",
			"British thermal unit (international table) per pound");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_POUND_DEGREE_FAHRENHEIT = init("J43",
			4863027, "BtuIT/(lb\u00B7\u00B0F)",
			"British thermal unit (international table) per pound degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_POUND_DEGREE_RANKINE = init("A21",
			4272689, "BtuIT/(lb\u00B7\u00B0R)", "British thermal unit (international table) per pound degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SECOND = init("J45", 4863029,
			"BtuIT/s", "British thermal unit (international table) per second");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SECOND_FOOT_DEGREE_RANKINE = init(
			"A22", 4272690, "BtuIT/(s\u00B7ft\u00B7\u00B0R)",
			"British thermal unit (international table) per second foot degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SECOND_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"N76", 5125942, "BtuIT/(s\u00B7ft\u00B2\u00B7\u00BAF)",
			"British thermal unit (international table) per second square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SECOND_SQUARE_FOOT_DEGREE_RANKINE = init(
			"A20", 4272688, "BtuIT/(s\u00B7ft\u00B2\u00B7\u00B0R)",
			"British thermal unit (international table) per second square foot degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SQUARE_FOOT = init("P37", 5255991,
			"BtuIT/ft\u00B2", "British thermal unit (international table) per square foot");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SQUARE_FOOT_HOUR = init("N50",
			5125424, "BtuIT/(ft\u00B2\u00B7h)", "British thermal unit (international table) per square foot hour");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SQUARE_FOOT_SECOND = init("N53",
			5125427, "BtuIT/(ft\u00B2\u00B7s)", "British thermal unit (international table) per square foot second");
	public static final EUInformation BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_PER_SQUARE_INCH_SECOND = init("N55",
			5125429, "BtuIT/(in\u00B2\u00B7s)", "British thermal unit (international table) per square inch second");
	public static final EUInformation BRITISH_THERMAL_UNIT_MEAN = init("J39", 4862777, "Btu",
			"British thermal unit (mean)");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_FOOT_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J46", 4863030, "Btuth\u00B7ft/(h\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (thermochemical) foot per hour square\u00A0foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_INCH_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J48", 4863032, "Btuth\u00B7in/(h\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (thermochemical) inch per hour square\u00A0foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_INCH_PER_SECOND_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"J49", 4863033, "Btuth\u00B7in/(s\u00B7ft\u00B2\u00B7\u00B0F)",
			"British thermal unit (thermochemical) inch per second\u00A0square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_CUBIC_FOOT = init("N59", 5125433,
			"Btuth/ft\u00B3", "British thermal unit (thermochemical) per cubic foot");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_DEGREE_FAHRENHEIT = init("N61", 5125681,
			"Btuth/\u00BAF", "British thermal unit (thermochemical) per degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_DEGREE_RANKINE = init("N63", 5125683,
			"Btuth/\u00BAR", "British thermal unit (thermochemical) per degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_HOUR = init("J47", 4863031, "Btuth/h",
			"British thermal unit (thermochemical) per hour");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_HOUR_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"N75", 5125941, "Btuth/(h\u00B7ft\u00B2\u00B7\u00BAF)",
			"British thermal unit (thermochemical) per hour square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_MINUTE = init("J51", 4863281, "Btuth/min",
			"British thermal unit (thermochemical) per minute");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_POUND = init("N73", 5125939, "Btuth/lb",
			"British thermal unit (thermochemical) per pound");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_POUND_DEGREE_FAHRENHEIT = init("J50",
			4863280, "Btuth/(lb\u00B7\u00B0F)", "British thermal unit (thermochemical) per pound degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_POUND_DEGREE_RANKINE = init("N64",
			5125684, "(Btuth/\u00B0R)/lb", "British thermal unit (thermochemical) per pound degree Rankine");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SECOND = init("J52", 4863282, "Btuth/s",
			"British thermal unit (thermochemical) per second");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SECOND_SQUARE_FOOT_DEGREE_FAHRENHEIT = init(
			"N77", 5125943, "Btuth/(s\u00B7ft\u00B2\u00B7\u00BAF)",
			"British thermal unit (thermochemical) per second square foot degree Fahrenheit");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SQUARE_FOOT = init("P38", 5255992,
			"Btuth/ft\u00B2", "British thermal unit (thermochemical) per square foot");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SQUARE_FOOT_HOUR = init("N51", 5125425,
			"Btuth/(ft\u00B2\u00B7h)", "British thermal unit (thermochemical) per square foot hour");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SQUARE_FOOT_MINUTE = init("N52", 5125426,
			"Btuth/(ft\u00B2\u00B7min)", "British thermal unit (thermochemical) per square foot minute");
	public static final EUInformation BRITISH_THERMAL_UNIT_THERMOCHEMICAL_PER_SQUARE_FOOT_SECOND = init("N54", 5125428,
			"Btuth/(ft\u00B2\u00B7s)", "British thermal unit (thermochemical) per square foot second");
	public static final EUInformation BULK_PACK = init("AB", 16706, "pk", "bulk pack");
	public static final EUInformation BUSHEL_UK = init("BUI", 4347209, "bushel (UK)", "bushel (UK)");
	public static final EUInformation BUSHEL_UK_PER_DAY = init("J64", 4863540, "bu (UK)/d", "bushel (UK) per day");
	public static final EUInformation BUSHEL_UK_PER_HOUR = init("J65", 4863541, "bu (UK)/h", "bushel (UK) per hour");
	public static final EUInformation BUSHEL_UK_PER_MINUTE = init("J66", 4863542, "bu (UK)/min",
			"bushel (UK) per minute");
	public static final EUInformation BUSHEL_UK_PER_SECOND = init("J67", 4863543, "bu (UK)/s",
			"bushel (UK) per second");
	public static final EUInformation BUSHEL_US = init("BUA", 4347201, "bu (US)", "bushel (US)");
	public static final EUInformation BUSHEL_US_DRY_PER_DAY = init("J68", 4863544, "bu (US dry)/d",
			"bushel (US dry) per day");
	public static final EUInformation BUSHEL_US_DRY_PER_HOUR = init("J69", 4863545, "bu (US dry)/h",
			"bushel (US dry) per hour");
	public static final EUInformation BUSHEL_US_DRY_PER_MINUTE = init("J70", 4863792, "bu (US dry)/min",
			"bushel (US dry) per minute");
	public static final EUInformation BUSHEL_US_DRY_PER_SECOND = init("J71", 4863793, "bu (US dry)/s",
			"bushel (US dry) per second");
	public static final EUInformation BYTE = init("AD", 16708, "byte", "byte");
	public static final EUInformation BYTE_PER_SECOND = init("P93", 5257523, "byte/s", "byte per second");
	public static final EUInformation CAKE = init("KA", 19265, "", "cake");
	public static final EUInformation CALL = init("C0", 17200, "", "call");
	public static final EUInformation CALORIE_20__CELSIUS_ = init("N69", 5125689, "cal\u2082\u2080",
			"calorie (20 \u00BAC)");
	public static final EUInformation CALORIE_INTERNATIONAL_TABLE_PER_GRAM_DEGREE_CELSIUS = init("J76", 4863798,
			"calIT/(g\u00B7\u00B0C)", "calorie (international table) per gram degree Celsius");
	public static final EUInformation CALORIE_MEAN = init("J75", 4863797, "cal", "calorie (mean)");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_CENTIMETRE_SECOND_DEGREE_CELSIUS = init("J78", 4863800,
			"calth/(cm\u00B7s\u00B7\u00B0C)", "calorie (thermochemical) per centimetre second degree Celsius");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_GRAM_DEGREE_CELSIUS = init("J79", 4863801,
			"calth/(g\u00B7\u00B0C)", "calorie (thermochemical) per gram degree Celsius");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_MINUTE = init("J81", 4864049, "calth/min",
			"calorie (thermochemical) per minute");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_SECOND = init("J82", 4864050, "calth/s",
			"calorie (thermochemical) per second");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_SQUARE_CENTIMETRE = init("P39", 5255993,
			"calth/cm\u00B2", "calorie (thermochemical) per square centimetre");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_SQUARE_CENTIMETRE_MINUTE = init("N56", 5125430,
			"calth/(cm\u00B2\u00B7min)", "calorie (thermochemical) per square centimetre minute");
	public static final EUInformation CALORIE_THERMOCHEMICAL_PER_SQUARE_CENTIMETRE_SECOND = init("N57", 5125431,
			"calth/(cm\u00B2\u00B7s)", "calorie (thermochemical) per square centimetre second");
	public static final EUInformation CANDELA = init("CDL", 4408396, "cd", "candela");
	public static final EUInformation CANDELA_PER_SQUARE_FOOT = init("P32", 5255986, "cd/ft\u00B2",
			"candela per square foot");
	public static final EUInformation CANDELA_PER_SQUARE_INCH = init("P28", 5255736, "cd/in\u00B2",
			"candela per square inch");
	public static final EUInformation CANDELA_PER_SQUARE_METRE = init("A24", 4272692, "cd/m\u00B2",
			"candela per square metre");
	public static final EUInformation CARD = init("CG", 17223, "", "card");
	public static final EUInformation CARRYING_CAPACITY_IN_METRIC_TON = init("CCT", 4408148, "",
			"carrying capacity in metric ton");
	public static final EUInformation CENTAL_UK = init("CNT", 4410964, "", "cental (UK)");
	public static final EUInformation CENTIGRAM = init("CGM", 4409165, "cg", "centigram");
	public static final EUInformation CENTILITRE = init("CLT", 4410452, "cl", "centilitre");
	public static final EUInformation CENTIMETRE = init("CMT", 4410708, "cm", "centimetre");
	public static final EUInformation CENTIMETRE_OF_MERCURY_0__CELSIUS_ = init("N13", 5124403, "cmHg (0 \u00BAC)",
			"centimetre of mercury (0 \u00BAC)");
	public static final EUInformation CENTIMETRE_OF_WATER_4__CELSIUS_ = init("N14", 5124404, "cmH\u2082O (4 \u00B0C)",
			"centimetre of water (4 \u00BAC)");
	public static final EUInformation CENTIMETRE_PER_BAR = init("G04", 4665396, "cm/bar", "centimetre per bar");
	public static final EUInformation CENTIMETRE_PER_HOUR = init("H49", 4731961, "cm/h", "centimetre per hour");
	public static final EUInformation CENTIMETRE_PER_KELVIN = init("F51", 4601137, "cm/K", "centimetre per kelvin");
	public static final EUInformation CENTIMETRE_PER_SECOND = init("2M", 12877, "cm/s", "centimetre per second");
	public static final EUInformation CENTIMETRE_PER_SECOND_BAR = init("J85", 4864053, "(cm/s)/bar",
			"centimetre per second bar");
	public static final EUInformation CENTIMETRE_PER_SECOND_KELVIN = init("J84", 4864052, "(cm/s)/K",
			"centimetre per second kelvin");
	public static final EUInformation CENTIMETRE_PER_SECOND_SQUARED = init("M39", 5059385, "cm/s\u00B2",
			"centimetre per second squared");
	public static final EUInformation CENTINEWTON_METRE = init("J72", 4863794, "cN\u00B7m", "centinewton metre");
	public static final EUInformation CENTIPOISE = init("C7", 17207, "cP", "centipoise");
	public static final EUInformation CENTIPOISE_PER_BAR = init("J74", 4863796, "cP/bar", "centipoise per bar");
	public static final EUInformation CENTIPOISE_PER_KELVIN = init("J73", 4863795, "cP/K", "centipoise per kelvin");
	public static final EUInformation CENTISTOKES = init("4C", 13379, "cSt", "centistokes");
	public static final EUInformation CHAIN_BASED_ON_US_SURVEY_FOOT = init("M49", 5059641, "ch (US survey)",
			"chain (based on U.S. survey foot)");
	public static final EUInformation CIRCULAR_MIL = init("M47", 5059639, "cmil", "circular mil");
	public static final EUInformation CLO = init("J83", 4864051, "clo", "clo");
	public static final EUInformation COIL_GROUP = init("C9", 17209, "", "coil group");
	public static final EUInformation COMMON_YEAR = init("L95", 4995381, "y (365 days)", "common year");
	public static final EUInformation CONTENT_GRAM = init("CTG", 4412487, "", "content gram");
	public static final EUInformation CONTENT_TON_METRIC = init("CTN", 4412494, "", "content ton (metric)");
	public static final EUInformation CONVENTIONAL_METRE_OF_WATER = init("N23", 5124659, "mH\u2082O",
			"conventional metre of water");
	public static final EUInformation CORD = init("WCD", 5718852, "", "cord");
	public static final EUInformation CORD_128_FT3 = init("M68", 5060152, "cord", "cord (128 ft3)");
	public static final EUInformation COULOMB = init("COU", 4411221, "C", "coulomb");
	public static final EUInformation COULOMB_METRE = init("A26", 4272694, "C\u00B7m", "coulomb metre");
	public static final EUInformation COULOMB_METRE_SQUARED_PER_VOLT = init("A27", 4272695, "C\u00B7m\u00B2/V",
			"coulomb metre squared per volt");
	public static final EUInformation COULOMB_PER_CUBIC_CENTIMETRE = init("A28", 4272696, "C/cm\u00B3",
			"coulomb per cubic centimetre");
	public static final EUInformation COULOMB_PER_CUBIC_METRE = init("A29", 4272697, "C/m\u00B3",
			"coulomb per cubic metre");
	public static final EUInformation COULOMB_PER_CUBIC_MILLIMETRE = init("A30", 4272944, "C/mm\u00B3",
			"coulomb per cubic millimetre");
	public static final EUInformation COULOMB_PER_KILOGRAM = init("CKG", 4410183, "C/kg", "coulomb per kilogram");
	public static final EUInformation COULOMB_PER_KILOGRAM_SECOND = init("A31", 4272945, "C/(kg\u00B7s)",
			"coulomb per kilogram second");
	public static final EUInformation COULOMB_PER_METRE = init("P10", 5255472, "C/m", "coulomb per metre");
	public static final EUInformation COULOMB_PER_MOLE = init("A32", 4272946, "C/mol", "coulomb per mole");
	public static final EUInformation COULOMB_PER_SQUARE_CENTIMETRE = init("A33", 4272947, "C/cm\u00B2",
			"coulomb per square centimetre");
	public static final EUInformation COULOMB_PER_SQUARE_METRE = init("A34", 4272948, "C/m\u00B2",
			"coulomb per square metre");
	public static final EUInformation COULOMB_PER_SQUARE_MILLIMETRE = init("A35", 4272949, "C/mm\u00B2",
			"coulomb per square millimetre");
	public static final EUInformation COULOMB_SQUARE_METRE_PER_KILOGRAM = init("J53", 4863283, "C\u00B7m\u00B2/kg",
			"coulomb square metre per kilogram");
	public static final EUInformation CREDIT = init("B17", 4337975, "", "credit");
	public static final EUInformation CUBIC_CENTIMETRE = init("CMQ", 4410705, "cm\u00B3", "cubic centimetre");
	public static final EUInformation CUBIC_CENTIMETRE_PER_BAR = init("G94", 4667700, "cm\u00B3/bar",
			"cubic centimetre per bar");
	public static final EUInformation CUBIC_CENTIMETRE_PER_CUBIC_METRE = init("J87", 4864055, "cm\u00B3/m\u00B3",
			"cubic centimetre per cubic metre");
	public static final EUInformation CUBIC_CENTIMETRE_PER_DAY = init("G47", 4666423, "cm\u00B3/d",
			"cubic centimetre per day");
	public static final EUInformation CUBIC_CENTIMETRE_PER_DAY_BAR = init("G78", 4667192, "cm\u00B3/(d\u00B7bar)",
			"cubic centimetre per day bar");
	public static final EUInformation CUBIC_CENTIMETRE_PER_DAY_KELVIN = init("G61", 4666929, "cm\u00B3/(d\u00B7K)",
			"cubic centimetre per day kelvin");
	public static final EUInformation CUBIC_CENTIMETRE_PER_HOUR = init("G48", 4666424, "cm\u00B3/h",
			"cubic centimetre per hour");
	public static final EUInformation CUBIC_CENTIMETRE_PER_HOUR_BAR = init("G79", 4667193, "cm\u00B3/(h\u00B7bar)",
			"cubic centimetre per hour bar");
	public static final EUInformation CUBIC_CENTIMETRE_PER_HOUR_KELVIN = init("G62", 4666930, "cm\u00B3/(h\u00B7K)",
			"cubic centimetre per hour kelvin");
	public static final EUInformation CUBIC_CENTIMETRE_PER_KELVIN = init("G27", 4665911, "cm\u00B3/K",
			"cubic centimetre per kelvin");
	public static final EUInformation CUBIC_CENTIMETRE_PER_MINUTE = init("G49", 4666425, "cm\u00B3/min",
			"cubic centimetre per minute");
	public static final EUInformation CUBIC_CENTIMETRE_PER_MINUTE_BAR = init("G80", 4667440, "cm\u00B3/(min\u00B7bar)",
			"cubic centimetre per minute bar");
	public static final EUInformation CUBIC_CENTIMETRE_PER_MINUTE_KELVIN = init("G63", 4666931, "cm\u00B3/(min\u00B7K)",
			"cubic centimetre per minute kelvin");
	public static final EUInformation CUBIC_CENTIMETRE_PER_MOLE = init("A36", 4272950, "cm\u00B3/mol",
			"cubic centimetre per mole");
	public static final EUInformation CUBIC_CENTIMETRE_PER_SECOND = init("2J", 12874, "cm\u00B3/s",
			"cubic centimetre per second");
	public static final EUInformation CUBIC_CENTIMETRE_PER_SECOND_BAR = init("G81", 4667441, "cm\u00B3/(s\u00B7bar)",
			"cubic centimetre per second bar");
	public static final EUInformation CUBIC_CENTIMETRE_PER_SECOND_KELVIN = init("G64", 4666932, "cm\u00B3/(s\u00B7K)",
			"cubic centimetre per second kelvin");
	public static final EUInformation CUBIC_DECAMETRE = init("DMA", 4476225, "dam\u00B3", "cubic decametre");
	public static final EUInformation CUBIC_DECIMETRE = init("DMQ", 4476241, "dm\u00B3", "cubic decimetre");
	public static final EUInformation CUBIC_DECIMETRE_PER_CUBIC_METRE = init("J91", 4864305, "dm\u00B3/m\u00B3",
			"cubic decimetre per cubic metre");
	public static final EUInformation CUBIC_DECIMETRE_PER_DAY = init("J90", 4864304, "dm\u00B3/d",
			"cubic decimetre per day");
	public static final EUInformation CUBIC_DECIMETRE_PER_HOUR = init("E92", 4536626, "dm\u00B3/h",
			"cubic decimetre per hour");
	public static final EUInformation CUBIC_DECIMETRE_PER_KILOGRAM = init("N28", 5124664, "dm\u00B3/kg",
			"cubic decimetre per kilogram");
	public static final EUInformation CUBIC_DECIMETRE_PER_MINUTE = init("J92", 4864306, "dm\u00B3/min",
			"cubic decimetre per minute");
	public static final EUInformation CUBIC_DECIMETRE_PER_MOLE = init("A37", 4272951, "dm\u00B3/mol",
			"cubic decimetre per mole");
	public static final EUInformation CUBIC_DECIMETRE_PER_SECOND = init("J93", 4864307, "dm\u00B3/s",
			"cubic decimetre per second");
	public static final EUInformation CUBIC_FOOT = init("FTQ", 4609105, "ft\u00B3", "cubic foot");
	public static final EUInformation CUBIC_FOOT_PER_DAY = init("K22", 4928050, "ft\u00B3/d", "cubic foot per day");
	public static final EUInformation CUBIC_FOOT_PER_DEGREE_FAHRENHEIT = init("K21", 4928049, "ft\u00B3/\u00B0F",
			"cubic foot per degree Fahrenheit");
	public static final EUInformation CUBIC_FOOT_PER_HOUR = init("2K", 12875, "ft\u00B3/h", "cubic foot per hour");
	public static final EUInformation CUBIC_FOOT_PER_MINUTE = init("2L", 12876, "ft\u00B3/min",
			"cubic foot per minute");
	public static final EUInformation CUBIC_FOOT_PER_POUND = init("N29", 5124665, "ft\u00B3/lb",
			"cubic foot per pound");
	public static final EUInformation CUBIC_FOOT_PER_PSI = init("K23", 4928051, "ft\u00B3/psi", "cubic foot per psi");
	public static final EUInformation CUBIC_FOOT_PER_SECOND = init("E17", 4534583, "ft\u00B3/s",
			"cubic foot per second");
	public static final EUInformation CUBIC_HECTOMETRE = init("H19", 4731193, "hm\u00B3", "cubic hectometre");
	public static final EUInformation CUBIC_INCH = init("INQ", 4804177, "in\u00B3", "cubic inch");
	public static final EUInformation CUBIC_INCH_PER_HOUR = init("G56", 4666678, "in\u00B3/h", "cubic inch per hour");
	public static final EUInformation CUBIC_INCH_PER_MINUTE = init("G57", 4666679, "in\u00B3/min",
			"cubic inch per minute");
	public static final EUInformation CUBIC_INCH_PER_POUND = init("N30", 5124912, "in\u00B3/lb",
			"cubic inch per pound");
	public static final EUInformation CUBIC_INCH_PER_SECOND = init("G58", 4666680, "in\u00B3/s",
			"cubic inch per second");
	public static final EUInformation CUBIC_KILOMETRE = init("H20", 4731440, "km\u00B3", "cubic kilometre");
	public static final EUInformation CUBIC_METRE = init("MTQ", 5067857, "m\u00B3", "cubic metre");
	public static final EUInformation CUBIC_METRE_PER_BAR = init("G96", 4667702, "m\u00B3/bar", "cubic metre per bar");
	public static final EUInformation CUBIC_METRE_PER_COULOMB = init("A38", 4272952, "m\u00B3/C",
			"cubic metre per coulomb");
	public static final EUInformation CUBIC_METRE_PER_CUBIC_METRE = init("H60", 4732464, "m\u00B3/m\u00B3",
			"cubic metre per cubic metre");
	public static final EUInformation CUBIC_METRE_PER_DAY = init("G52", 4666674, "m\u00B3/d", "cubic metre per day");
	public static final EUInformation CUBIC_METRE_PER_DAY_BAR = init("G86", 4667446, "m\u00B3/(d\u00B7bar)",
			"cubic metre per day bar");
	public static final EUInformation CUBIC_METRE_PER_DAY_KELVIN = init("G69", 4666937, "m\u00B3/(d\u00B7K)",
			"cubic metre per day kelvin");
	public static final EUInformation CUBIC_METRE_PER_HOUR = init("MQH", 5067080, "m\u00B3/h", "cubic metre per hour");
	public static final EUInformation CUBIC_METRE_PER_HOUR_BAR = init("G87", 4667447, "m\u00B3/(h\u00B7bar)",
			"cubic metre per hour bar");
	public static final EUInformation CUBIC_METRE_PER_HOUR_KELVIN = init("G70", 4667184, "m\u00B3/(h\u00B7K)",
			"cubic metre per hour kelvin");
	public static final EUInformation CUBIC_METRE_PER_KELVIN = init("G29", 4665913, "m\u00B3/K",
			"cubic metre per kelvin");
	public static final EUInformation CUBIC_METRE_PER_KILOGRAM = init("A39", 4272953, "m\u00B3/kg",
			"cubic metre per kilogram");
	public static final EUInformation CUBIC_METRE_PER_MINUTE = init("G53", 4666675, "m\u00B3/min",
			"cubic metre per minute");
	public static final EUInformation CUBIC_METRE_PER_MINUTE_BAR = init("G88", 4667448, "m\u00B3/(min\u00B7bar)",
			"cubic metre per minute bar");
	public static final EUInformation CUBIC_METRE_PER_MINUTE_KELVIN = init("G71", 4667185, "m\u00B3/(min\u00B7K)",
			"cubic metre per minute kelvin");
	public static final EUInformation CUBIC_METRE_PER_MOLE = init("A40", 4273200, "m\u00B3/mol",
			"cubic metre per mole");
	public static final EUInformation CUBIC_METRE_PER_PASCAL = init("M71", 5060401, "m\u00B3/Pa",
			"cubic metre per pascal");
	public static final EUInformation CUBIC_METRE_PER_SECOND = init("MQS", 5067091, "m\u00B3/s",
			"cubic metre per second");
	public static final EUInformation CUBIC_METRE_PER_SECOND_BAR = init("G89", 4667449, "m\u00B3/(s\u00B7bar)",
			"cubic metre per second bar");
	public static final EUInformation CUBIC_METRE_PER_SECOND_KELVIN = init("G72", 4667186, "m\u00B3/(s\u00B7K)",
			"cubic metre per second kelvin");
	public static final EUInformation CUBIC_METRE_PER_SECOND_PASCAL = init("N45", 5125173, "(m\u00B3/s)/Pa",
			"cubic metre per second pascal");
	public static final EUInformation CUBIC_METRE_PER_SECOND_SQUARE_METRE = init("P87", 5257271, "(m\u00B3/s)/m\u00B2",
			"cubic metre per second square metre");
	public static final EUInformation CUBIC_MILE_UK_STATUTE = init("M69", 5060153, "mi\u00B3",
			"cubic mile (UK statute)");
	public static final EUInformation CUBIC_MILLIMETRE = init("MMQ", 5066065, "mm\u00B3", "cubic millimetre");
	public static final EUInformation CUBIC_MILLIMETRE_PER_CUBIC_METRE = init("L21", 4993585, "mm\u00B3/m\u00B3",
			"cubic millimetre per cubic metre");
	public static final EUInformation CUBIC_YARD = init("YDQ", 5850193, "yd\u00B3", "cubic yard");
	public static final EUInformation CUBIC_YARD_PER_DAY = init("M12", 5058866, "yd\u00B3/d", "cubic yard per day");
	public static final EUInformation CUBIC_YARD_PER_DEGREE_FAHRENHEIT = init("M11", 5058865, "yd\u00B3/\u00B0F",
			"cubic yard per degree Fahrenheit");
	public static final EUInformation CUBIC_YARD_PER_HOUR = init("M13", 5058867, "yd\u00B3/h", "cubic yard per hour");
	public static final EUInformation CUBIC_YARD_PER_MINUTE = init("M15", 5058869, "yd\u00B3/min",
			"cubic yard per minute");
	public static final EUInformation CUBIC_YARD_PER_PSI = init("M14", 5058868, "yd\u00B3/psi", "cubic yard per psi");
	public static final EUInformation CUBIC_YARD_PER_SECOND = init("M16", 5058870, "yd\u00B3/s",
			"cubic yard per second");
	public static final EUInformation CUP_UNIT_OF_VOLUME = init("G21", 4665905, "cup (US)", "cup [unit of volume]");
	public static final EUInformation CURIE = init("CUR", 4412754, "Ci", "curie");
	public static final EUInformation CURIE_PER_KILOGRAM = init("A42", 4273202, "Ci/kg", "curie per kilogram");
	public static final EUInformation CYCLE = init("B7", 16951, "", "cycle");
	public static final EUInformation DAY = init("DAY", 4473177, "d", "day");
	public static final EUInformation DEADWEIGHT_TONNAGE = init("A43", 4273203, "dwt", "deadweight tonnage");
	public static final EUInformation DECADE = init("DEC", 4474179, "", "decade");
	public static final EUInformation DECADE_LOGARITHMIC = init("P41", 5256241, "dec", "decade (logarithmic)");
	public static final EUInformation DECAGRAM = init("DJ", 17482, "dag", "decagram");
	public static final EUInformation DECALITRE = init("A44", 4273204, "dal", "decalitre");
	public static final EUInformation DECAMETRE = init("A45", 4273205, "dam", "decametre");
	public static final EUInformation DECAPASCAL = init("H75", 4732725, "daPa", "decapascal");
	public static final EUInformation DECARE = init("DAA", 4473153, "daa", "decare");
	public static final EUInformation DECIBEL = init("2N", 12878, "dB", "decibel");
	public static final EUInformation DECIBEL_PER_KILOMETRE = init("H51", 4732209, "dB/km", "decibel per kilometre");
	public static final EUInformation DECIBEL_PER_METRE = init("H52", 4732210, "dB/m", "decibel per metre");
	public static final EUInformation DECIGRAM = init("DG", 17479, "dg", "decigram");
	public static final EUInformation DECILITRE = init("DLT", 4475988, "dl", "decilitre");
	public static final EUInformation DECILITRE_PER_GRAM = init("22", 12850, "dl/g", "decilitre per gram");
	public static final EUInformation DECIMETRE = init("DMT", 4476244, "dm", "decimetre");
	public static final EUInformation DECINEWTON_METRE = init("DN", 17486, "dN\u00B7m", "decinewton metre");
	public static final EUInformation DECITEX = init("A47", 4273207, "dtex (g/10km)", "decitex");
	public static final EUInformation DECITONNE = init("DTN", 4478030, "dt or dtn", "decitonne");
	public static final EUInformation DEGREE_API = init("J13", 4862259, "\u00B0API", "degree API");
	public static final EUInformation DEGREE_BALLING = init("J17", 4862263, "\u00B0Balling", "degree Balling");
	public static final EUInformation DEGREE_BAUME_ORIGIN_SCALE = init("J14", 4862260, "\u00B0B\u00E9",
			"degree Baume (origin scale)");
	public static final EUInformation DEGREE_BAUME_US_HEAVY = init("J15", 4862261, "\u00B0B\u00E9 (US heavy)",
			"degree Baume (US heavy)");
	public static final EUInformation DEGREE_BAUME_US_LIGHT = init("J16", 4862262, "\u00B0B\u00E9 (US light)",
			"degree Baume (US light)");
	public static final EUInformation DEGREE_BRIX = init("J18", 4862264, "\u00B0Bx", "degree Brix");
	public static final EUInformation DEGREE_CELSIUS = init("CEL", 4408652, "\u00B0C", "degree Celsius");
	public static final EUInformation DEGREE_CELSIUS_PER_BAR = init("F60", 4601392, "\u00B0C/bar",
			"degree Celsius per bar");
	public static final EUInformation DEGREE_CELSIUS_PER_HOUR = init("H12", 4731186, "\u00B0C/h",
			"degree Celsius per hour");
	public static final EUInformation DEGREE_CELSIUS_PER_KELVIN = init("E98", 4536632, "\u00B0C/K",
			"degree Celsius per kelvin");
	public static final EUInformation DEGREE_CELSIUS_PER_MINUTE = init("H13", 4731187, "\u00B0C/min",
			"degree Celsius per minute");
	public static final EUInformation DEGREE_CELSIUS_PER_SECOND = init("H14", 4731188, "\u00B0C/s",
			"degree Celsius per second");
	public static final EUInformation DEGREE_DAY = init("E10", 4534576, "deg da", "degree day");
	public static final EUInformation DEGREE_FAHRENHEIT = init("FAH", 4604232, "\u00B0F", "degree Fahrenheit");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_PER_BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE = init("N84",
			5126196, "\u00BAF/(BtuIT/h)", "degree Fahrenheit hour per British thermal unit (international table)");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_PER_BRITISH_THERMAL_UNIT_THERMOCHEMICAL = init("N85",
			5126197, "\u00BAF/(Btuth/h)", "degree Fahrenheit hour per British thermal unit (thermochemical)");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_SQUARE_FOOT_PER_BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE = init(
			"J22", 4862514, "\u00B0F\u00B7h\u00B7ft\u00B2/BtuIT",
			"degree Fahrenheit hour square foot per British thermal unit (international table)");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_SQUARE_FOOT_PER_BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE_INCH = init(
			"N88", 5126200, "\u00BAF\u00B7h\u00B7ft\u00B2/(BtuIT\u00B7in)",
			"degree Fahrenheit hour square foot per British thermal unit (international table) inch");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_SQUARE_FOOT_PER_BRITISH_THERMAL_UNIT_THERMOCHEMICAL = init(
			"J19", 4862265, "\u00B0F\u00B7h\u00B7ft\u00B2/Btuth",
			"degree Fahrenheit hour square foot per British thermal unit (thermochemical)");
	public static final EUInformation DEGREE_FAHRENHEIT_HOUR_SQUARE_FOOT_PER_BRITISH_THERMAL_UNIT_THERMOCHEMICAL_INCH = init(
			"N89", 5126201, "\u00BAF\u00B7h\u00B7ft\u00B2/(Btuth\u00B7in)",
			"degree Fahrenheit hour square foot per British thermal unit (thermochemical) inch");
	public static final EUInformation DEGREE_FAHRENHEIT_PER_BAR = init("J21", 4862513, "\u00B0F/bar",
			"degree Fahrenheit per bar");
	public static final EUInformation DEGREE_FAHRENHEIT_PER_HOUR = init("J23", 4862515, "\u00B0F/h",
			"degree Fahrenheit per hour");
	public static final EUInformation DEGREE_FAHRENHEIT_PER_KELVIN = init("J20", 4862512, "\u00B0F/K",
			"degree Fahrenheit per kelvin");
	public static final EUInformation DEGREE_FAHRENHEIT_PER_MINUTE = init("J24", 4862516, "\u00B0F/min",
			"degree Fahrenheit per minute");
	public static final EUInformation DEGREE_FAHRENHEIT_PER_SECOND = init("J25", 4862517, "\u00B0F/s",
			"degree Fahrenheit per second");
	public static final EUInformation DEGREE_FAHRENHEIT_SECOND_PER_BRITISH_THERMAL_UNIT_INTERNATIONAL_TABLE = init(
			"N86", 5126198, "\u00BAF/(BtuIT/s)",
			"degree Fahrenheit second per British thermal unit (international table)");
	public static final EUInformation DEGREE_FAHRENHEIT_SECOND_PER_BRITISH_THERMAL_UNIT_THERMOCHEMICAL = init("N87",
			5126199, "\u00BAF/(Btuth/s)", "degree Fahrenheit second per British thermal unit (thermochemical)");
	public static final EUInformation DEGREE_OECHSLE = init("J27", 4862519, "\u00B0Oechsle", "degree Oechsle");
	public static final EUInformation DEGREE_PER_METRE = init("H27", 4731447, "\u00B0/m", "degree per metre");
	public static final EUInformation DEGREE_PER_SECOND = init("E96", 4536630, "\u00B0/s", "degree per second");
	public static final EUInformation DEGREE_PLATO = init("PLA", 5262401, "\u00B0P", "degree Plato");
	public static final EUInformation DEGREE_RANKINE = init("A48", 4273208, "\u00B0R", "degree Rankine");
	public static final EUInformation DEGREE_RANKINE_PER_HOUR = init("J28", 4862520, "\u00B0R/h",
			"degree Rankine per hour");
	public static final EUInformation DEGREE_RANKINE_PER_MINUTE = init("J29", 4862521, "\u00B0R/min",
			"degree Rankine per minute");
	public static final EUInformation DEGREE_RANKINE_PER_SECOND = init("J30", 4862768, "\u00B0R/s",
			"degree Rankine per second");
	public static final EUInformation DEGREE_TWADDELL = init("J31", 4862769, "\u00B0Tw", "degree Twaddell");
	public static final EUInformation DEGREE_UNIT_OF_ANGLE = init("DD", 17476, "\u00B0", "degree [unit of angle]");
	public static final EUInformation DEGREE_UNIT_OF_ANGLE_PER_SECOND_SQUARED = init("M45", 5059637, "\u00B0/s\u00B2",
			"degree [unit of angle] per second squared");
	public static final EUInformation DENIER = init("A49", 4273209, "den (g/9 km)", "denier");
	public static final EUInformation DIGIT = init("B19", 4337977, "", "digit");
	public static final EUInformation DIOPTRE = init("Q25", 5321269, "dpt", "dioptre");
	public static final EUInformation DISPLACEMENT_TONNAGE = init("DPT", 4477012, "", "displacement tonnage");
	public static final EUInformation DOSE = init("E27", 4534839, "", "dose");
	public static final EUInformation DOTS_PER_INCH = init("E39", 4535097, "dpi", "dots per inch");
	public static final EUInformation DOZEN = init("DZN", 4479566, "DOZ", "dozen");
	public static final EUInformation DOZEN_PACK = init("DZP", 4479568, "", "dozen pack");
	public static final EUInformation DOZEN_PAIR = init("DPR", 4477010, "", "dozen pair");
	public static final EUInformation DOZEN_PIECE = init("DPC", 4476995, "", "dozen piece");
	public static final EUInformation DOZEN_ROLL = init("DRL", 4477516, "", "dozen roll");
	public static final EUInformation DRAM_UK = init("DRI", 4477513, "", "dram (UK)");
	public static final EUInformation DRAM_US = init("DRA", 4477505, "", "dram (US)");
	public static final EUInformation DRY_BARREL_US = init("BLD", 4344900, "bbl (US)", "dry barrel (US)");
	public static final EUInformation DRY_GALLON_US = init("GLD", 4672580, "dry gal (US)", "dry gallon (US)");
	public static final EUInformation DRY_PINT_US = init("PTD", 5264452, "dry pt (US)", "dry pint (US)");
	public static final EUInformation DRY_POUND = init("DB", 17474, "", "dry pound");
	public static final EUInformation DRY_QUART_US = init("QTD", 5329988, "dry qt (US)", "dry quart (US)");
	public static final EUInformation DRY_TON = init("DT", 17492, "", "dry ton");
	public static final EUInformation DYNE_METRE = init("M97", 5060919, "dyn\u00B7m", "dyne metre");
	public static final EUInformation EACH = init("EA", 17729, "", "each");
	public static final EUInformation ELECTRONIC_MAIL_BOX = init("EB", 17730, "", "electronic mail box");
	public static final EUInformation ELECTRONVOLT = init("A53", 4273459, "eV", "electronvolt");
	public static final EUInformation ELECTRONVOLT_PER_METRE = init("A54", 4273460, "eV/m", "electronvolt per metre");
	public static final EUInformation ELECTRONVOLT_SQUARE_METRE = init("A55", 4273461, "eV\u00B7m\u00B2",
			"electronvolt square metre");
	public static final EUInformation ELECTRONVOLT_SQUARE_METRE_PER_KILOGRAM = init("A56", 4273462,
			"eV\u00B7m\u00B2/kg", "electronvolt square metre per kilogram");
	public static final EUInformation EQUIVALENT_GALLON = init("EQ", 17745, "", "equivalent gallon");
	public static final EUInformation ERLANG = init("Q11", 5321009, "E", "erlang");
	public static final EUInformation EXABIT_PER_SECOND = init("E58", 4535608, "Ebit/s", "exabit per second");
	public static final EUInformation EXAJOULE = init("A68", 4273720, "EJ", "exajoule");
	public static final EUInformation EXBIBIT_PER_CUBIC_METRE = init("E67", 4535863, "Eibit/m\u00B3",
			"exbibit per cubic metre");
	public static final EUInformation EXBIBIT_PER_METRE = init("E65", 4535861, "Eibit/m", "exbibit per metre");
	public static final EUInformation EXBIBIT_PER_SQUARE_METRE = init("E66", 4535862, "Eibit/m\u00B2",
			"exbibit per square metre");
	public static final EUInformation EXBIBYTE = init("E59", 4535609, "Eibyte", "exbibyte");
	public static final EUInformation FAILURES_IN_TIME = init("FIT", 4606292, "FIT", "failures in time");
	public static final EUInformation FARAD = init("FAR", 4604242, "F", "farad");
	public static final EUInformation FARAD_PER_KILOMETRE = init("H33", 4731699, "F/km", "farad per kilometre");
	public static final EUInformation FARAD_PER_METRE = init("A69", 4273721, "F/m", "farad per metre");
	public static final EUInformation FATHOM = init("AK", 16715, "fth", "fathom");
	public static final EUInformation FEMTOJOULE = init("A70", 4273968, "fJ", "femtojoule");
	public static final EUInformation FEMTOLITRE = init("Q32", 5321522, "fl", "femtolitre");
	public static final EUInformation FEMTOMETRE = init("A71", 4273969, "fm", "femtometre");
	public static final EUInformation FIBRE_METRE = init("FBM", 4604493, "", "fibre metre");
	public static final EUInformation FIVE_PACK = init("P5", 20533, "", "five pack");
	public static final EUInformation FIXED_RATE = init("1I", 12617, "", "fixed rate");
	public static final EUInformation FLAKE_TON = init("FL", 17996, "", "flake ton");
	public static final EUInformation FLUID_OUNCE_UK = init("OZI", 5200457, "fl oz (UK)", "fluid ounce (UK)");
	public static final EUInformation FLUID_OUNCE_US = init("OZA", 5200449, "fl oz (US)", "fluid ounce (US)");
	public static final EUInformation FOOT = init("FOT", 4607828, "ft", "foot");
	public static final EUInformation FOOTCANDLE = init("P27", 5255735, "ftc", "footcandle");
	public static final EUInformation FOOTLAMBERT = init("P29", 5255737, "ftL", "footlambert");
	public static final EUInformation FOOT_OF_WATER_392__FAHRENHEIT_ = init("N15", 5124405, "ftH\u2082O (39,2 \u00BAF)",
			"foot of water (39.2 \u00BAF)");
	public static final EUInformation FOOT_PER_DEGREE_FAHRENHEIT = init("K13", 4927795, "ft/\u00B0F",
			"foot per degree Fahrenheit");
	public static final EUInformation FOOT_PER_HOUR = init("K14", 4927796, "ft/h", "foot per hour");
	public static final EUInformation FOOT_PER_MINUTE = init("FR", 18002, "ft/min", "foot per minute");
	public static final EUInformation FOOT_PER_PSI = init("K17", 4927799, "ft/psi", "foot per psi");
	public static final EUInformation FOOT_PER_SECOND = init("FS", 18003, "ft/s", "foot per second");
	public static final EUInformation FOOT_PER_SECOND_DEGREE_FAHRENHEIT = init("K18", 4927800, "(ft/s)/\u00B0F",
			"foot per second degree Fahrenheit");
	public static final EUInformation FOOT_PER_SECOND_PSI = init("K19", 4927801, "(ft/s)/psi", "foot per second psi");
	public static final EUInformation FOOT_PER_SECOND_SQUARED = init("A73", 4273971, "ft/s\u00B2",
			"foot per second squared");
	public static final EUInformation FOOT_PER_THOUSAND = init("E33", 4535091, "", "foot per thousand");
	public static final EUInformation FOOT_POUNDAL = init("N46", 5125174, "ft\u00B7pdl", "foot poundal");
	public static final EUInformation FOOT_POUND_FORCE = init("85", 14389, "ft\u00B7lbf", "foot pound-force");
	public static final EUInformation FOOT_POUND_FORCE_PER_HOUR = init("K15", 4927797, "ft\u00B7lbf/h",
			"foot pound-force per hour");
	public static final EUInformation FOOT_POUND_FORCE_PER_MINUTE = init("K16", 4927798, "ft\u00B7lbf/min",
			"foot pound-force per minute");
	public static final EUInformation FOOT_POUND_FORCE_PER_SECOND = init("A74", 4273972, "ft\u00B7lbf/s",
			"foot pound-force per second");
	public static final EUInformation FOOT_TO_THE_FOURTH_POWER = init("N27", 5124663, "ft\u2074",
			"foot to the fourth power");
	public static final EUInformation FOOT_US_SURVEY = init("M51", 5059889, "ft (US survey)", "foot (U.S. survey)");
	public static final EUInformation FORTY_FOOT_CONTAINER = init("21", 12849, "", "forty foot container");
	public static final EUInformation FRANKLIN = init("N94", 5126452, "Fr", "franklin");
	public static final EUInformation FREIGHT_TON = init("A75", 4273973, "", "freight ton");
	public static final EUInformation FRENCH_GAUGE = init("H79", 4732729, "Fg", "French gauge");
	public static final EUInformation FURLONG = init("M50", 5059888, "fur", "furlong");
	public static final EUInformation GAL = init("A76", 4273974, "Gal", "gal");
	public static final EUInformation GALLON_UK = init("GLI", 4672585, "gal (UK)", "gallon (UK)");
	public static final EUInformation GALLON_UK_PER_DAY = init("K26", 4928054, "gal (UK)/d", "gallon (UK) per day");
	public static final EUInformation GALLON_UK_PER_HOUR = init("K27", 4928055, "gal (UK)/h", "gallon (UK) per hour");
	public static final EUInformation GALLON_UK_PER_SECOND = init("K28", 4928056, "gal (UK)/s",
			"gallon (UK) per second");
	public static final EUInformation GALLON_US = init("GLL", 4672588, "gal (US)", "gallon (US)");
	public static final EUInformation GALLON_US_LIQUID_PER_SECOND = init("K30", 4928304, "gal (US liq.)/s",
			"gallon (US liquid) per second");
	public static final EUInformation GALLON_US_PER_DAY = init("GB", 18242, "gal (US)/d", "gallon (US) per day");
	public static final EUInformation GALLON_US_PER_HOUR = init("G50", 4666672, "gal/h", "gallon (US) per hour");
	public static final EUInformation GAMMA = init("P12", 5255474, "\u03B3", "gamma");
	public static final EUInformation GIBIBIT = init("B30", 4338480, "Gibit", "gibibit");
	public static final EUInformation GIBIBIT_PER_CUBIC_METRE = init("E71", 4536113, "Gibit/m\u00B3",
			"gibibit per cubic metre");
	public static final EUInformation GIBIBIT_PER_METRE = init("E69", 4535865, "Gibit/m", "gibibit per metre");
	public static final EUInformation GIBIBIT_PER_SQUARE_METRE = init("E70", 4536112, "Gibit/m\u00B2",
			"gibibit per square metre");
	public static final EUInformation GIBIBYTE = init("E62", 4535858, "Gibyte", "gibibyte");
	public static final EUInformation GIGABECQUEREL = init("GBQ", 4670033, "GBq", "gigabecquerel");
	public static final EUInformation GIGABIT = init("B68", 4339256, "Gbit", "gigabit");
	public static final EUInformation GIGABIT_PER_SECOND = init("B80", 4339760, "Gbit/s", "gigabit per second");
	public static final EUInformation GIGABYTE = init("E34", 4535092, "Gbyte", "gigabyte");
	public static final EUInformation GIGABYTE_PER_SECOND = init("E68", 4535864, "Gbyte/s", "gigabyte per second");
	public static final EUInformation GIGACOULOMB_PER_CUBIC_METRE = init("A84", 4274228, "GC/m\u00B3",
			"gigacoulomb per cubic metre");
	public static final EUInformation GIGAELECTRONVOLT = init("A85", 4274229, "GeV", "gigaelectronvolt");
	public static final EUInformation GIGAHERTZ = init("A86", 4274230, "GHz", "gigahertz");
	public static final EUInformation GIGAHERTZ_METRE = init("M18", 5058872, "GHz\u00B7m", "gigahertz metre");
	public static final EUInformation GIGAJOULE = init("GV", 18262, "GJ", "gigajoule");
	public static final EUInformation GIGAOHM = init("A87", 4274231, "G\u2126", "gigaohm");
	public static final EUInformation GIGAOHM_METRE = init("A88", 4274232, "G\u2126\u00B7m", "gigaohm metre");
	public static final EUInformation GIGAOHM_PER_METRE = init("M26", 5059126, "G\u2126/m", "gigaohm per metre");
	public static final EUInformation GIGAPASCAL = init("A89", 4274233, "GPa", "gigapascal");
	public static final EUInformation GIGAWATT = init("A90", 4274480, "GW", "gigawatt");
	public static final EUInformation GIGAWATT_HOUR = init("GWH", 4675400, "GW\u00B7h", "gigawatt hour");
	public static final EUInformation GILBERT = init("N97", 5126455, "Gi", "gilbert");
	public static final EUInformation GILL_UK = init("GII", 4671817, "gi (UK)", "gill (UK)");
	public static final EUInformation GILL_UK_PER_DAY = init("K32", 4928306, "gi (UK)/d", "gill (UK) per day");
	public static final EUInformation GILL_UK_PER_HOUR = init("K33", 4928307, "gi (UK)/h", "gill (UK) per hour");
	public static final EUInformation GILL_UK_PER_MINUTE = init("K34", 4928308, "gi (UK)/min", "gill (UK) per minute");
	public static final EUInformation GILL_UK_PER_SECOND = init("K35", 4928309, "gi (UK)/s", "gill (UK) per second");
	public static final EUInformation GILL_US = init("GIA", 4671809, "gi (US)", "gill (US)");
	public static final EUInformation GILL_US_PER_DAY = init("K36", 4928310, "gi (US)/d", "gill (US) per day");
	public static final EUInformation GILL_US_PER_HOUR = init("K37", 4928311, "gi (US)/h", "gill (US) per hour");
	public static final EUInformation GILL_US_PER_MINUTE = init("K38", 4928312, "gi (US)/min", "gill (US) per minute");
	public static final EUInformation GILL_US_PER_SECOND = init("K39", 4928313, "gi (US)/s", "gill (US) per second");
	public static final EUInformation GON = init("A91", 4274481, "gon", "gon");
	public static final EUInformation GRAIN = init("GRN", 4674126, "gr", "grain");
	public static final EUInformation GRAIN_PER_GALLON_US = init("K41", 4928561, "gr/gal (US)",
			"grain per gallon (US)");
	public static final EUInformation GRAM = init("GRM", 4674125, "g", "gram");
	public static final EUInformation GRAM_CENTIMETRE_PER_SECOND = init("M99", 5060921, "g\u00B7(cm/s)",
			"gram centimetre per second");
	public static final EUInformation GRAM_DRY_WEIGHT = init("GDW", 4670551, "", "gram, dry weight");
	public static final EUInformation GRAM_FORCE_PER_SQUARE_CENTIMETRE = init("K31", 4928305, "gf/cm\u00B2",
			"gram-force per square centimetre");
	public static final EUInformation GRAM_INCLUDING_CONTAINER = init("GIC", 4671811, "", "gram, including container");
	public static final EUInformation GRAM_INCLUDING_INNER_PACKAGING = init("GIP", 4671824, "",
			"gram, including inner packaging");
	public static final EUInformation GRAM_MILLIMETRE = init("H84", 4732980, "g\u00B7mm", "gram millimetre");
	public static final EUInformation GRAM_OF_FISSILE_ISOTOPE = init("GFI", 4671049, "gi F/S",
			"gram of fissile isotope");
	public static final EUInformation GRAM_PER_BAR = init("F74", 4601652, "g/bar", "gram per bar");
	public static final EUInformation GRAM_PER_CENTIMETRE_SECOND = init("N41", 5125169, "g/(cm\u00B7s)",
			"gram per centimetre second");
	public static final EUInformation GRAM_PER_CUBIC_CENTIMETRE = init("23", 12851, "g/cm\u00B3",
			"gram per cubic centimetre");
	public static final EUInformation GRAM_PER_CUBIC_CENTIMETRE_BAR = init("G11", 4665649, "g/(cm\u00B3\u00B7bar)",
			"gram per cubic centimetre bar");
	public static final EUInformation GRAM_PER_CUBIC_CENTIMETRE_KELVIN = init("G33", 4666163, "g/(cm\u00B3\u00B7K)",
			"gram per cubic centimetre kelvin");
	public static final EUInformation GRAM_PER_CUBIC_DECIMETRE = init("F23", 4600371, "g/dm\u00B3",
			"gram per cubic decimetre");
	public static final EUInformation GRAM_PER_CUBIC_DECIMETRE_BAR = init("G12", 4665650, "g/(dm\u00B3\u00B7bar)",
			"gram per cubic decimetre bar");
	public static final EUInformation GRAM_PER_CUBIC_DECIMETRE_KELVIN = init("G34", 4666164, "g/(dm\u00B3\u00B7K)",
			"gram per cubic decimetre kelvin");
	public static final EUInformation GRAM_PER_CUBIC_METRE = init("A93", 4274483, "g/m\u00B3", "gram per cubic metre");
	public static final EUInformation GRAM_PER_CUBIC_METRE_BAR = init("G14", 4665652, "g/(m\u00B3\u00B7bar)",
			"gram per cubic metre bar");
	public static final EUInformation GRAM_PER_CUBIC_METRE_KELVIN = init("G36", 4666166, "g/(m\u00B3\u00B7K)",
			"gram per cubic metre kelvin");
	public static final EUInformation GRAM_PER_DAY = init("F26", 4600374, "g/d", "gram per day");
	public static final EUInformation GRAM_PER_DAY_BAR = init("F62", 4601394, "g/(d\u00B7bar)", "gram per day bar");
	public static final EUInformation GRAM_PER_DAY_KELVIN = init("F35", 4600629, "g/(d\u00B7K)", "gram per day kelvin");
	public static final EUInformation GRAM_PER_HERTZ = init("F25", 4600373, "g/Hz", "gram per hertz");
	public static final EUInformation GRAM_PER_HOUR = init("F27", 4600375, "g/h", "gram per hour");
	public static final EUInformation GRAM_PER_HOUR_BAR = init("F63", 4601395, "g/(h\u00B7bar)", "gram per hour bar");
	public static final EUInformation GRAM_PER_HOUR_KELVIN = init("F36", 4600630, "g/(h\u00B7K)",
			"gram per hour kelvin");
	public static final EUInformation GRAM_PER_KELVIN = init("F14", 4600116, "g/K", "gram per kelvin");
	public static final EUInformation GRAM_PER_LITRE = init("GL", 18252, "g/l", "gram per litre");
	public static final EUInformation GRAM_PER_LITRE_BAR = init("G13", 4665651, "g/(l\u00B7bar)", "gram per litre bar");
	public static final EUInformation GRAM_PER_LITRE_KELVIN = init("G35", 4666165, "g/(l\u00B7K)",
			"gram per litre kelvin");
	public static final EUInformation GRAM_PER_METRE_GRAM_PER_100_CENTIMETRES = init("GF", 18246, "g/m",
			"gram per metre (gram per 100 centimetres)");
	public static final EUInformation GRAM_PER_MILLILITRE = init("GJ", 18250, "g/ml", "gram per millilitre");
	public static final EUInformation GRAM_PER_MILLILITRE_BAR = init("G15", 4665653, "g/(ml\u00B7bar)",
			"gram per millilitre bar");
	public static final EUInformation GRAM_PER_MILLILITRE_KELVIN = init("G37", 4666167, "g/(ml\u00B7K)",
			"gram per millilitre kelvin");
	public static final EUInformation GRAM_PER_MILLIMETRE = init("H76", 4732726, "g/mm", "gram per millimetre");
	public static final EUInformation GRAM_PER_MINUTE = init("F28", 4600376, "g/min", "gram per minute");
	public static final EUInformation GRAM_PER_MINUTE_BAR = init("F64", 4601396, "g/(min\u00B7bar)",
			"gram per minute bar");
	public static final EUInformation GRAM_PER_MINUTE_KELVIN = init("F37", 4600631, "g/(min\u00B7K)",
			"gram per minute kelvin");
	public static final EUInformation GRAM_PER_MOLE = init("A94", 4274484, "g/mol", "gram per mole");
	public static final EUInformation GRAM_PER_SECOND = init("F29", 4600377, "g/s", "gram per second");
	public static final EUInformation GRAM_PER_SECOND_BAR = init("F65", 4601397, "g/(s\u00B7bar)",
			"gram per second bar");
	public static final EUInformation GRAM_PER_SECOND_KELVIN = init("F38", 4600632, "g/(s\u00B7K)",
			"gram per second kelvin");
	public static final EUInformation GRAM_PER_SQUARE_CENTIMETRE = init("25", 12853, "g/cm\u00B2",
			"gram per square centimetre");
	public static final EUInformation GRAM_PER_SQUARE_METRE = init("GM", 18253, "g/m\u00B2", "gram per square metre");
	public static final EUInformation GRAM_PER_SQUARE_MILLIMETRE = init("N24", 5124660, "g/mm\u00B2",
			"gram per square millimetre");
	public static final EUInformation GRAY = init("A95", 4274485, "Gy", "gray");
	public static final EUInformation GRAY_PER_HOUR = init("P61", 5256753, "Gy/h", "gray per hour");
	public static final EUInformation GRAY_PER_MINUTE = init("P57", 5256503, "Gy/min", "gray per minute");
	public static final EUInformation GRAY_PER_SECOND = init("A96", 4274486, "Gy/s", "gray per second");
	public static final EUInformation GREAT_GROSS = init("GGR", 4671314, "", "great gross");
	public static final EUInformation GROSS = init("GRO", 4674127, "gr", "gross");
	public static final EUInformation GROSS_KILOGRAM = init("E4", 17716, "", "gross kilogram");
	public static final EUInformation GROUP = init("10", 12592, "", "group");
	public static final EUInformation GUNTERS_CHAIN = init("X1", 22577, "ch (UK)", "Gunter's chain");
	public static final EUInformation HALF_YEAR_6_MONTHS = init("SAN", 5456206, "", "half year (6 months)");
	public static final EUInformation HANGING_CONTAINER = init("Z11", 5910833, "", "hanging container");
	public static final EUInformation HANK = init("HA", 18497, "", "hank");
	public static final EUInformation HARTLEY = init("Q15", 5321013, "Hart", "hartley");
	public static final EUInformation HARTLEY_PER_SECOND = init("Q18", 5321016, "Hart/s", "hartley per second");
	public static final EUInformation HEAD = init("HEA", 4736321, "", "head");
	public static final EUInformation HECTOBAR = init("HBA", 4735553, "hbar", "hectobar");
	public static final EUInformation HECTOGRAM = init("HGM", 4736845, "hg", "hectogram");
	public static final EUInformation HECTOLITRE = init("HLT", 4738132, "hl", "hectolitre");
	public static final EUInformation HECTOLITRE_OF_PURE_ALCOHOL = init("HPA", 4739137, "",
			"hectolitre of pure alcohol");
	public static final EUInformation HECTOMETRE = init("HMT", 4738388, "hm", "hectometre");
	public static final EUInformation HECTOPASCAL = init("A97", 4274487, "hPa", "hectopascal");
	public static final EUInformation HECTOPASCAL_CUBIC_METRE_PER_SECOND = init("F94", 4602164, "hPa\u00B7m\u00B3/s",
			"hectopascal cubic metre per second");
	public static final EUInformation HECTOPASCAL_LITRE_PER_SECOND = init("F93", 4602163, "hPa\u00B7l/s",
			"hectopascal litre per second");
	public static final EUInformation HECTOPASCAL_PER_BAR = init("E99", 4536633, "hPa/bar", "hectopascal per bar");
	public static final EUInformation HECTOPASCAL_PER_KELVIN = init("F82", 4601906, "hPa/K", "hectopascal per kelvin");
	public static final EUInformation HECTOPASCAL_PER_METRE = init("P82", 5257266, "hPa/m", "hectopascal per metre");
	public static final EUInformation HEFNER_KERZE = init("P35", 5255989, "HK", "Hefner-Kerze");
	public static final EUInformation HENRY = init("81", 14385, "H", "henry");
	public static final EUInformation HENRY_PER_KILOOHM = init("H03", 4730931, "H/k\u2126", "henry per kiloohm");
	public static final EUInformation HENRY_PER_METRE = init("A98", 4274488, "H/m", "henry per metre");
	public static final EUInformation HENRY_PER_OHM = init("H04", 4730932, "H/\u2126", "henry per ohm");
	public static final EUInformation HERTZ = init("HTZ", 4740186, "Hz", "hertz");
	public static final EUInformation HERTZ_METRE = init("H34", 4731700, "Hz\u00B7m", "hertz metre");
	public static final EUInformation HORSEPOWER_BOILER = init("K42", 4928562, "boiler hp", "horsepower (boiler)");
	public static final EUInformation HORSEPOWER_ELECTRIC = init("K43", 4928563, "electric hp",
			"horsepower (electric)");
	public static final EUInformation HOUR = init("HUR", 4740434, "h", "hour");
	public static final EUInformation HUNDRED = init("CEN", 4408654, "", "hundred");
	public static final EUInformation HUNDRED_BOARD_FOOT = init("BP", 16976, "", "hundred board foot");
	public static final EUInformation HUNDRED_BOXES = init("HBX", 4735576, "", "hundred boxes");
	public static final EUInformation HUNDRED_COUNT = init("HC", 18499, "", "hundred count");
	public static final EUInformation HUNDRED_CUBIC_FOOT = init("HH", 18504, "", "hundred cubic foot");
	public static final EUInformation HUNDRED_CUBIC_METRE = init("FF", 17990, "", "hundred cubic metre");
	public static final EUInformation HUNDRED_INTERNATIONAL_UNIT = init("HIU", 4737365, "",
			"hundred international unit");
	public static final EUInformation HUNDRED_KILOGRAM_DRY_WEIGHT = init("HDW", 4736087, "",
			"hundred kilogram, dry weight");
	public static final EUInformation HUNDRED_KILOGRAM_NET_MASS = init("HKM", 4737869, "",
			"hundred kilogram, net mass");
	public static final EUInformation HUNDRED_LEAVE = init("CLF", 4410438, "", "hundred leave");
	public static final EUInformation HUNDRED_METRE = init("JPS", 4870227, "", "hundred metre");
	public static final EUInformation HUNDRED_PACK = init("CNP", 4410960, "", "hundred pack");
	public static final EUInformation HUNDRED_POUND_CWT__PER__HUNDRED_WEIGHT_US = init("CWA", 4413249, "cwt (US)",
			"hundred pound (cwt) / hundred weight (US)");
	public static final EUInformation HUNDRED_WEIGHT_UK = init("CWI", 4413257, "cwt (UK)", "hundred weight (UK)");
	public static final EUInformation HYDRAULIC_HORSE_POWER = init("5J", 13642, "", "hydraulic horse power");
	public static final EUInformation IMPERIAL_GALLON_PER_MINUTE = init("G3", 18227, "gal (UK) /min",
			"Imperial gallon per minute");
	public static final EUInformation INCH = init("INH", 4804168, "in", "inch");
	public static final EUInformation INCH_OF_MERCURY = init("F79", 4601657, "inHg", "inch of mercury");
	public static final EUInformation INCH_OF_MERCURY_32__FAHRENHEIT_ = init("N16", 5124406, "inHG (32 \u00BAF)",
			"inch of mercury (32 \u00BAF)");
	public static final EUInformation INCH_OF_MERCURY_60__FAHRENHEIT_ = init("N17", 5124407, "inHg (60 \u00BAF)",
			"inch of mercury (60 \u00BAF)");
	public static final EUInformation INCH_OF_WATER = init("F78", 4601656, "inH\u2082O", "inch of water");
	public static final EUInformation INCH_OF_WATER_392__FAHRENHEIT_ = init("N18", 5124408, "inH\u2082O (39,2 \u00BAF)",
			"inch of water (39.2 \u00BAF)");
	public static final EUInformation INCH_OF_WATER_60__FAHRENHEIT_ = init("N19", 5124409, "inH\u2082O (60 \u00BAF)",
			"inch of water (60 \u00BAF)");
	public static final EUInformation INCH_PER_DEGREE_FAHRENHEIT = init("K45", 4928565, "in/\u00B0F",
			"inch per degree Fahrenheit");
	public static final EUInformation INCH_PER_LINEAR_FOOT = init("B82", 4339762, "", "inch per linear foot");
	public static final EUInformation INCH_PER_MINUTE = init("M63", 5060147, "in/min", "inch per minute");
	public static final EUInformation INCH_PER_PSI = init("K46", 4928566, "in/psi", "inch per psi");
	public static final EUInformation INCH_PER_SECOND = init("IU", 18773, "in/s", "inch per second");
	public static final EUInformation INCH_PER_SECOND_DEGREE_FAHRENHEIT = init("K47", 4928567, "(in/s)/\u00B0F",
			"inch per second degree Fahrenheit");
	public static final EUInformation INCH_PER_SECOND_PSI = init("K48", 4928568, "(in/s)/psi", "inch per second psi");
	public static final EUInformation INCH_PER_SECOND_SQUARED = init("IV", 18774, "in/s\u00B2",
			"inch per second squared");
	public static final EUInformation INCH_PER_TWO_PI_RADIANT = init("H57", 4732215, "in/revolution",
			"inch per two pi radiant");
	public static final EUInformation INCH_PER_YEAR = init("M61", 5060145, "in/y", "inch per year");
	public static final EUInformation INCH_POUNDAL = init("N47", 5125175, "in\u00B7pdl", "inch poundal");
	public static final EUInformation INCH_POUND_POUND_INCH = init("IA", 18753, "in\u00B7lb",
			"inch pound (pound inch)");
	public static final EUInformation INCH_TO_THE_FOURTH_POWER = init("D69", 4470329, "in\u2074",
			"inch to the fourth power");
	public static final EUInformation INTERNATIONAL_CANDLE = init("P36", 5255990, "IK", "international candle");
	public static final EUInformation INTERNATIONAL_SUGAR_DEGREE = init("ISD", 4805444, "",
			"international sugar degree");
	public static final EUInformation JOB = init("E51", 4535601, "", "job");
	public static final EUInformation JOULE = init("JOU", 4869973, "J", "joule");
	public static final EUInformation JOULE_PER_CUBIC_METRE = init("B8", 16952, "J/m\u00B3", "joule per cubic metre");
	public static final EUInformation JOULE_PER_DAY = init("P17", 5255479, "J/d", "joule per day");
	public static final EUInformation JOULE_PER_GRAM = init("D95", 4471093, "J/g", "joule per gram");
	public static final EUInformation JOULE_PER_HOUR = init("P16", 5255478, "J/h", "joule per hour");
	public static final EUInformation JOULE_PER_KELVIN = init("JE", 19013, "J/K", "joule per kelvin");
	public static final EUInformation JOULE_PER_KILOGRAM = init("J2", 18994, "J/kg", "joule per kilogram");
	public static final EUInformation JOULE_PER_KILOGRAM_KELVIN = init("B11", 4337969, "J/(kg\u00B7K)",
			"joule per kilogram kelvin");
	public static final EUInformation JOULE_PER_METRE = init("B12", 4337970, "J/m", "joule per metre");
	public static final EUInformation JOULE_PER_METRE_TO_THE_FOURTH_POWER = init("B14", 4337972, "J/m\u2074",
			"joule per metre to the fourth power");
	public static final EUInformation JOULE_PER_MINUTE = init("P15", 5255477, "J/min", "joule per minute");
	public static final EUInformation JOULE_PER_MOLE = init("B15", 4337973, "J/mol", "joule per mole");
	public static final EUInformation JOULE_PER_MOLE_KELVIN = init("B16", 4337974, "J/(mol\u00B7K)",
			"joule per mole kelvin");
	public static final EUInformation JOULE_PER_SECOND = init("P14", 5255476, "J/s", "joule per second");
	public static final EUInformation JOULE_PER_SQUARE_CENTIMETRE = init("E43", 4535347, "J/cm\u00B2",
			"joule per square centimetre");
	public static final EUInformation JOULE_PER_SQUARE_METRE = init("B13", 4337971, "J/m\u00B2",
			"joule per square metre");
	public static final EUInformation JOULE_PER_TESLA = init("Q10", 5321008, "J/T", "joule per tesla");
	public static final EUInformation JOULE_SECOND = init("B18", 4337976, "J\u00B7s", "joule second");
	public static final EUInformation JOULE_SQUARE_METRE = init("D73", 4470579, "J\u00B7m\u00B2", "joule square metre");
	public static final EUInformation JOULE_SQUARE_METRE_PER_KILOGRAM = init("B20", 4338224, "J\u00B7m\u00B2/kg",
			"joule square metre per kilogram");
	public static final EUInformation KATAL = init("KAT", 4931924, "kat", "katal");
	public static final EUInformation KELVIN = init("KEL", 4932940, "K", "kelvin");
	public static final EUInformation KELVIN_METRE_PER_WATT = init("H35", 4731701, "K\u00B7m/W",
			"kelvin metre per watt");
	public static final EUInformation KELVIN_PER_BAR = init("F61", 4601393, "K/bar", "kelvin per bar");
	public static final EUInformation KELVIN_PER_HOUR = init("F10", 4600112, "K/h", "kelvin per hour");
	public static final EUInformation KELVIN_PER_KELVIN = init("F02", 4599858, "K/K", "kelvin per kelvin");
	public static final EUInformation KELVIN_PER_MINUTE = init("F11", 4600113, "K/min", "kelvin per minute");
	public static final EUInformation KELVIN_PER_PASCAL = init("N79", 5125945, "K/Pa", "kelvin per pascal");
	public static final EUInformation KELVIN_PER_SECOND = init("F12", 4600114, "K/s", "kelvin per second");
	public static final EUInformation KELVIN_PER_WATT = init("B21", 4338225, "K/W", "kelvin per watt");
	public static final EUInformation KIBIBIT = init("C21", 4403761, "Kibit", "kibibit");
	public static final EUInformation KIBIBIT_PER_CUBIC_METRE = init("E74", 4536116, "Kibit/m\u00B3",
			"kibibit per cubic metre");
	public static final EUInformation KIBIBIT_PER_METRE = init("E72", 4536114, "Kibit/m", "kibibit per metre");
	public static final EUInformation KIBIBIT_PER_SQUARE_METRE = init("E73", 4536115, "Kibit/m\u00B2",
			"kibibit per square metre");
	public static final EUInformation KIBIBYTE = init("E64", 4535860, "Kibyte", "kibibyte");
	public static final EUInformation KILOAMPERE = init("B22", 4338226, "kA", "kiloampere");
	public static final EUInformation KILOAMPERE_HOUR_THOUSAND_AMPERE_HOUR = init("TAH", 5521736, "kA\u00B7h",
			"kiloampere hour (thousand ampere hour)");
	public static final EUInformation KILOAMPERE_PER_METRE = init("B24", 4338228, "kA/m", "kiloampere per metre");
	public static final EUInformation KILOAMPERE_PER_SQUARE_METRE = init("B23", 4338227, "kA/m\u00B2",
			"kiloampere per square metre");
	public static final EUInformation KILOBAR = init("KBA", 4932161, "kbar", "kilobar");
	public static final EUInformation KILOBAUD = init("K50", 4928816, "kBd", "kilobaud");
	public static final EUInformation KILOBECQUEREL = init("2Q", 12881, "kBq", "kilobecquerel");
	public static final EUInformation KILOBECQUEREL_PER_KILOGRAM = init("B25", 4338229, "kBq/kg",
			"kilobecquerel per kilogram");
	public static final EUInformation KILOBIT = init("C37", 4404023, "kbit", "kilobit");
	public static final EUInformation KILOBIT_PER_SECOND = init("C74", 4405044, "kbit/s", "kilobit per second");
	public static final EUInformation KILOBYTE = init("2P", 12880, "kbyte", "kilobyte");
	public static final EUInformation KILOBYTE_PER_SECOND = init("P94", 5257524, "kbyte/s", "kilobyte per second");
	public static final EUInformation KILOCALORIE_INTERNATIONAL_TABLE = init("E14", 4534580, "kcalIT",
			"kilocalorie (international table)");
	public static final EUInformation KILOCALORIE_INTERNATIONAL_TABLE_PER_GRAM_KELVIN = init("N65", 5125685,
			"(kcalIT/K)/g", "kilocalorie (international table) per gram kelvin");
	public static final EUInformation KILOCALORIE_INTERNATIONAL_TABLE_PER_HOUR_METRE_DEGREE_CELSIUS = init("K52",
			4928818, "kcal/(m\u00B7h\u00B7\u00B0C)", "kilocalorie (international table) per hour metre degree Celsius");
	public static final EUInformation KILOCALORIE_MEAN = init("K51", 4928817, "kcal", "kilocalorie (mean)");
	public static final EUInformation KILOCALORIE_THERMOCHEMICAL = init("K53", 4928819, "kcalth",
			"kilocalorie (thermochemical)");
	public static final EUInformation KILOCALORIE_THERMOCHEMICAL_PER_HOUR = init("E15", 4534581, "kcalth/h",
			"kilocalorie (thermochemical) per hour");
	public static final EUInformation KILOCALORIE_THERMOCHEMICAL_PER_MINUTE = init("K54", 4928820, "kcalth/min",
			"kilocalorie (thermochemical) per minute");
	public static final EUInformation KILOCALORIE_THERMOCHEMICAL_PER_SECOND = init("K55", 4928821, "kcalth/s",
			"kilocalorie (thermochemical) per second");
	public static final EUInformation KILOCANDELA = init("P33", 5255987, "kcd", "kilocandela");
	public static final EUInformation KILOCHARACTER = init("KB", 19266, "", "kilocharacter");
	public static final EUInformation KILOCOULOMB = init("B26", 4338230, "kC", "kilocoulomb");
	public static final EUInformation KILOCOULOMB_PER_CUBIC_METRE = init("B27", 4338231, "kC/m\u00B3",
			"kilocoulomb per cubic metre");
	public static final EUInformation KILOCOULOMB_PER_SQUARE_METRE = init("B28", 4338232, "kC/m\u00B2",
			"kilocoulomb per square metre");
	public static final EUInformation KILOCURIE = init("2R", 12882, "kCi", "kilocurie");
	public static final EUInformation KILOELECTRONVOLT = init("B29", 4338233, "keV", "kiloelectronvolt");
	public static final EUInformation KILOFARAD = init("N90", 5126448, "kF", "kilofarad");
	public static final EUInformation KILOGRAM = init("KGM", 4933453, "kg", "kilogram");
	public static final EUInformation KILOGRAM_CENTIMETRE_PER_SECOND = init("M98", 5060920, "kg\u00B7(cm/s)",
			"kilogram centimetre per second");
	public static final EUInformation KILOGRAM_DRAINED_NET_WEIGHT = init("KDW", 4932695, "kg/net eda",
			"kilogram drained net weight");
	public static final EUInformation KILOGRAM_DRY_WEIGHT = init("MND", 5066308, "", "kilogram, dry weight");
	public static final EUInformation KILOGRAM_FORCE_METRE_PER_SQUARE_CENTIMETRE = init("E44", 4535348,
			"kgf\u00B7m/cm\u00B2", "kilogram-force metre per square centimetre");
	public static final EUInformation KILOGRAM_FORCE_PER_SQUARE_CENTIMETRE = init("E42", 4535346, "kgf/cm\u00B2",
			"kilogram-force per square centimetre");
	public static final EUInformation KILOGRAM_FORCE_PER_SQUARE_MILLIMETRE = init("E41", 4535345, "kgf/mm\u00B2",
			"kilogram-force per square millimetre");
	public static final EUInformation KILOGRAM_INCLUDING_CONTAINER = init("KIC", 4933955, "",
			"kilogram, including container");
	public static final EUInformation KILOGRAM_INCLUDING_INNER_PACKAGING = init("KIP", 4933968, "",
			"kilogram, including inner packaging");
	public static final EUInformation KILOGRAM_METRE = init("M94", 5060916, "kg\u00B7m", "kilogram metre");
	public static final EUInformation KILOGRAM_METRE_PER_SECOND = init("B31", 4338481, "kg\u00B7m/s",
			"kilogram metre per second");
	public static final EUInformation KILOGRAM_METRE_PER_SECOND_SQUARED = init("M77", 5060407, "kg\u00B7m/s\u00B2",
			"kilogram metre per second squared");
	public static final EUInformation KILOGRAM_METRE_SQUARED = init("B32", 4338482, "kg\u00B7m\u00B2",
			"kilogram metre squared");
	public static final EUInformation KILOGRAM_METRE_SQUARED_PER_SECOND = init("B33", 4338483, "kg\u00B7m\u00B2/s",
			"kilogram metre squared per second");
	public static final EUInformation KILOGRAM_NAMED_SUBSTANCE = init("KNS", 4935251, "", "kilogram named substance");
	public static final EUInformation KILOGRAM_OF_CHOLINE_CHLORIDE = init("KCC", 4932419,
			"kg C\u2085 H\u2081\u2084ClNO", "kilogram of choline chloride");
	public static final EUInformation KILOGRAM_OF_HYDROGEN_PEROXIDE = init("KHY", 4933721, "kg H\u2082O\u2082",
			"kilogram of hydrogen peroxide");
	public static final EUInformation KILOGRAM_OF_IMPORTED_MEAT_LESS_OFFAL = init("TMS", 5524819, "",
			"kilogram of imported meat, less offal");
	public static final EUInformation KILOGRAM_OF_METHYLAMINE = init("KMA", 4934977, "kg met.am.",
			"kilogram of methylamine");
	public static final EUInformation KILOGRAM_OF_NITROGEN = init("KNI", 4935241, "kg N", "kilogram of nitrogen");
	public static final EUInformation KILOGRAM_OF_PHOSPHORUS_PENTOXIDE_PHOSPHORIC_ANHYDRIDE = init("KPP", 4935760, "",
			"kilogram of phosphorus pentoxide (phosphoric anhydride)");
	public static final EUInformation KILOGRAM_OF_POTASSIUM_HYDROXIDE_CAUSTIC_POTASH = init("KPH", 4935752, "kg KOH",
			"kilogram of potassium hydroxide (caustic potash)");
	public static final EUInformation KILOGRAM_OF_POTASSIUM_OXIDE = init("KPO", 4935759, "kg K\u2082O",
			"kilogram of potassium oxide");
	public static final EUInformation KILOGRAM_OF_SODIUM_HYDROXIDE_CAUSTIC_SODA = init("KSH", 4936520, "kg NaOH",
			"kilogram of sodium hydroxide (caustic soda)");
	public static final EUInformation KILOGRAM_OF_SUBSTANCE_90__PERCENT__DRY = init("KSD", 4936516, "kg 90 % sdt",
			"kilogram of substance 90 % dry");
	public static final EUInformation KILOGRAM_OF_TUNGSTEN_TRIOXIDE = init("KWO", 4937551, "kg WO\u2083",
			"kilogram of tungsten trioxide");
	public static final EUInformation KILOGRAM_OF_URANIUM = init("KUR", 4937042, "kg U", "kilogram of uranium");
	public static final EUInformation KILOGRAM_PER_BAR = init("H53", 4732211, "kg/bar", "kilogram per bar");
	public static final EUInformation KILOGRAM_PER_CUBIC_CENTIMETRE = init("G31", 4666161, "kg/cm\u00B3",
			"kilogram per cubic centimetre");
	public static final EUInformation KILOGRAM_PER_CUBIC_CENTIMETRE_BAR = init("G16", 4665654, "kg/(cm\u00B3\u00B7bar)",
			"kilogram per cubic centimetre bar");
	public static final EUInformation KILOGRAM_PER_CUBIC_CENTIMETRE_KELVIN = init("G38", 4666168,
			"kg/(cm\u00B3\u00B7K)", "kilogram per cubic centimetre kelvin");
	public static final EUInformation KILOGRAM_PER_CUBIC_DECIMETRE = init("B34", 4338484, "kg/dm\u00B3",
			"kilogram per cubic decimetre");
	public static final EUInformation KILOGRAM_PER_CUBIC_DECIMETRE_BAR = init("H55", 4732213, "(kg/dm\u00B3)/bar",
			"kilogram per cubic decimetre bar");
	public static final EUInformation KILOGRAM_PER_CUBIC_DECIMETRE_KELVIN = init("H54", 4732212, "(kg/dm\u00B3)/K",
			"kilogram per cubic decimetre kelvin");
	public static final EUInformation KILOGRAM_PER_CUBIC_METRE = init("KMQ", 4934993, "kg/m\u00B3",
			"kilogram per cubic metre");
	public static final EUInformation KILOGRAM_PER_CUBIC_METRE_BAR = init("G18", 4665656, "kg/(m\u00B3\u00B7bar)",
			"kilogram per cubic metre bar");
	public static final EUInformation KILOGRAM_PER_CUBIC_METRE_KELVIN = init("G40", 4666416, "kg/(m\u00B3\u00B7K)",
			"kilogram per cubic metre kelvin");
	public static final EUInformation KILOGRAM_PER_CUBIC_METRE_PASCAL = init("M73", 5060403, "(kg/m\u00B3)/Pa",
			"kilogram per cubic metre pascal");
	public static final EUInformation KILOGRAM_PER_DAY = init("F30", 4600624, "kg/d", "kilogram per day");
	public static final EUInformation KILOGRAM_PER_DAY_BAR = init("F66", 4601398, "kg/(d\u00B7bar)",
			"kilogram per day bar");
	public static final EUInformation KILOGRAM_PER_DAY_KELVIN = init("F39", 4600633, "kg/(d\u00B7K)",
			"kilogram per day kelvin");
	public static final EUInformation KILOGRAM_PER_HOUR = init("E93", 4536627, "kg/h", "kilogram per hour");
	public static final EUInformation KILOGRAM_PER_HOUR_BAR = init("F67", 4601399, "kg/(h\u00B7bar)",
			"kilogram per hour bar");
	public static final EUInformation KILOGRAM_PER_HOUR_KELVIN = init("F40", 4600880, "kg/(h\u00B7K)",
			"kilogram per hour kelvin");
	public static final EUInformation KILOGRAM_PER_KELVIN = init("F15", 4600117, "kg/K", "kilogram per kelvin");
	public static final EUInformation KILOGRAM_PER_KILOGRAM = init("M29", 5059129, "kg/kg", "kilogram per kilogram");
	public static final EUInformation KILOGRAM_PER_KILOMETRE = init("M31", 5059377, "kg/km", "kilogram per kilometre");
	public static final EUInformation KILOGRAM_PER_KILOMOL = init("F24", 4600372, "kg/kmol", "kilogram per kilomol");
	public static final EUInformation KILOGRAM_PER_LITRE = init("B35", 4338485, "kg/l or kg/L", "kilogram per litre");
	public static final EUInformation KILOGRAM_PER_LITRE_BAR = init("G17", 4665655, "kg/(l\u00B7bar)",
			"kilogram per litre bar");
	public static final EUInformation KILOGRAM_PER_LITRE_KELVIN = init("G39", 4666169, "kg/(l\u00B7K)",
			"kilogram per litre kelvin");
	public static final EUInformation KILOGRAM_PER_METRE = init("KL", 19276, "kg/m", "kilogram per metre");
	public static final EUInformation KILOGRAM_PER_METRE_DAY = init("N39", 5124921, "kg/(m\u00B7d)",
			"kilogram per metre day");
	public static final EUInformation KILOGRAM_PER_METRE_HOUR = init("N40", 5125168, "kg/(m\u00B7h)",
			"kilogram per metre hour");
	public static final EUInformation KILOGRAM_PER_METRE_MINUTE = init("N38", 5124920, "kg/(m\u00B7min)",
			"kilogram per metre minute");
	public static final EUInformation KILOGRAM_PER_METRE_SECOND = init("N37", 5124919, "kg/(m\u00B7s)",
			"kilogram per metre second");
	public static final EUInformation KILOGRAM_PER_MILLIMETRE = init("KW", 19287, "kg/mm", "kilogram per millimetre");
	public static final EUInformation KILOGRAM_PER_MILLIMETRE_WIDTH = init("KI", 19273, "",
			"kilogram per millimetre width");
	public static final EUInformation KILOGRAM_PER_MINUTE = init("F31", 4600625, "kg/min", "kilogram per minute");
	public static final EUInformation KILOGRAM_PER_MINUTE_BAR = init("F68", 4601400, "kg/(min\u00B7bar)",
			"kilogram per minute bar");
	public static final EUInformation KILOGRAM_PER_MINUTE_KELVIN = init("F41", 4600881, "kg/(min\u00B7K)",
			"kilogram per minute kelvin");
	public static final EUInformation KILOGRAM_PER_MOLE = init("D74", 4470580, "kg/mol", "kilogram per mole");
	public static final EUInformation KILOGRAM_PER_PASCAL = init("M74", 5060404, "kg/Pa", "kilogram per pascal");
	public static final EUInformation KILOGRAM_PER_SECOND = init("KGS", 4933459, "kg/s", "kilogram per second");
	public static final EUInformation KILOGRAM_PER_SECOND_BAR = init("F69", 4601401, "kg/(s\u00B7bar)",
			"kilogram per second bar");
	public static final EUInformation KILOGRAM_PER_SECOND_KELVIN = init("F42", 4600882, "kg/(s\u00B7K)",
			"kilogram per second kelvin");
	public static final EUInformation KILOGRAM_PER_SECOND_PASCAL = init("M87", 5060663, "(kg/s)/Pa",
			"kilogram per second pascal");
	public static final EUInformation KILOGRAM_PER_SQUARE_CENTIMETRE = init("D5", 17461, "kg/cm\u00B2",
			"kilogram per square centimetre");
	public static final EUInformation KILOGRAM_PER_SQUARE_METRE = init("28", 12856, "kg/m\u00B2",
			"kilogram per square metre");
	public static final EUInformation KILOGRAM_PER_SQUARE_METRE_PASCAL_SECOND = init("Q28", 5321272,
			"kg/(m\u00B2\u00B7Pa\u00B7s)", "kilogram per square metre pascal second");
	public static final EUInformation KILOGRAM_PER_SQUARE_METRE_SECOND = init("H56", 4732214, "kg/(m\u00B2\u00B7s)",
			"kilogram per square metre second");
	public static final EUInformation KILOGRAM_SQUARE_CENTIMETRE = init("F18", 4600120, "kg\u00B7cm\u00B2",
			"kilogram square centimetre");
	public static final EUInformation KILOGRAM_SQUARE_MILLIMETRE = init("F19", 4600121, "kg\u00B7mm\u00B2",
			"kilogram square millimetre");
	public static final EUInformation KILOHENRY = init("P24", 5255732, "kH", "kilohenry");
	public static final EUInformation KILOHERTZ = init("KHZ", 4933722, "kHz", "kilohertz");
	public static final EUInformation KILOHERTZ_METRE = init("M17", 5058871, "kHz\u00B7m", "kilohertz metre");
	public static final EUInformation KILOJOULE = init("KJO", 4934223, "kJ", "kilojoule");
	public static final EUInformation KILOJOULE_PER_DAY = init("P21", 5255729, "kJ/d", "kilojoule per day");
	public static final EUInformation KILOJOULE_PER_GRAM = init("Q31", 5321521, "kJ/g", "kilojoule per gram");
	public static final EUInformation KILOJOULE_PER_HOUR = init("P20", 5255728, "kJ/h", "kilojoule per hour");
	public static final EUInformation KILOJOULE_PER_KELVIN = init("B41", 4338737, "kJ/K", "kilojoule per kelvin");
	public static final EUInformation KILOJOULE_PER_KILOGRAM = init("B42", 4338738, "kJ/kg", "kilojoule per kilogram");
	public static final EUInformation KILOJOULE_PER_KILOGRAM_KELVIN = init("B43", 4338739, "kJ/(kg\u00B7K)",
			"kilojoule per kilogram kelvin");
	public static final EUInformation KILOJOULE_PER_MINUTE = init("P19", 5255481, "kJ/min", "kilojoule per minute");
	public static final EUInformation KILOJOULE_PER_MOLE = init("B44", 4338740, "kJ/mol", "kilojoule per mole");
	public static final EUInformation KILOJOULE_PER_SECOND = init("P18", 5255480, "kJ/s", "kilojoule per second");
	public static final EUInformation KILOLITRE = init("K6", 19254, "kl", "kilolitre");
	public static final EUInformation KILOLITRE_PER_HOUR = init("4X", 13400, "kl/h", "kilolitre per hour");
	public static final EUInformation KILOLUX = init("KLX", 4934744, "klx", "kilolux");
	public static final EUInformation KILOMETRE = init("KMT", 4934996, "km", "kilometre");
	public static final EUInformation KILOMETRE_PER_HOUR = init("KMH", 4934984, "km/h", "kilometre per hour");
	public static final EUInformation KILOMETRE_PER_SECOND = init("M62", 5060146, "km/s", "kilometre per second");
	public static final EUInformation KILOMETRE_PER_SECOND_SQUARED = init("M38", 5059384, "km/s\u00B2",
			"kilometre per second squared");
	public static final EUInformation KILOMOLE = init("B45", 4338741, "kmol", "kilomole");
	public static final EUInformation KILOMOLE_PER_CUBIC_METRE = init("B46", 4338742, "kmol/m\u00B3",
			"kilomole per cubic metre");
	public static final EUInformation KILOMOLE_PER_CUBIC_METRE_BAR = init("K60", 4929072, "(kmol/m\u00B3)/bar",
			"kilomole per cubic metre bar");
	public static final EUInformation KILOMOLE_PER_CUBIC_METRE_KELVIN = init("K59", 4928825, "(kmol/m\u00B3)/K",
			"kilomole per cubic metre kelvin");
	public static final EUInformation KILOMOLE_PER_HOUR = init("K58", 4928824, "kmol/h", "kilomole per hour");
	public static final EUInformation KILOMOLE_PER_KILOGRAM = init("P47", 5256247, "kmol/kg", "kilomole per kilogram");
	public static final EUInformation KILOMOLE_PER_MINUTE = init("K61", 4929073, "kmol/min", "kilomole per minute");
	public static final EUInformation KILOMOLE_PER_SECOND = init("E94", 4536628, "kmol/s", "kilomole per second");
	public static final EUInformation KILONEWTON = init("B47", 4338743, "kN", "kilonewton");
	public static final EUInformation KILONEWTON_METRE = init("B48", 4338744, "kN\u00B7m", "kilonewton metre");
	public static final EUInformation KILONEWTON_PER_METRE = init("N31", 5124913, "kN/m", "kilonewton per metre");
	public static final EUInformation KILONEWTON_PER_SQUARE_METRE = init("KNM", 4935245, "kN/m2",
			"kilonewton per square metre");
	public static final EUInformation KILOOHM = init("B49", 4338745, "k\u2126", "kiloohm");
	public static final EUInformation KILOOHM_METRE = init("B50", 4338992, "k\u2126\u00B7m", "kiloohm metre");
	public static final EUInformation KILOPASCAL = init("KPA", 4935745, "kPa", "kilopascal");
	public static final EUInformation KILOPASCAL_PER_BAR = init("F03", 4599859, "kPa/bar", "kilopascal per bar");
	public static final EUInformation KILOPASCAL_PER_KELVIN = init("F83", 4601907, "kPa/K", "kilopascal per kelvin");
	public static final EUInformation KILOPASCAL_PER_METRE = init("P81", 5257265, "kPa/m", "kilopascal per metre");
	public static final EUInformation KILOPASCAL_PER_MILLIMETRE = init("34", 13108, "kPa/mm",
			"kilopascal per millimetre");
	public static final EUInformation KILOPASCAL_SQUARE_METRE_PER_GRAM = init("33", 13107, "kPa\u00B7m\u00B2/g",
			"kilopascal square metre per gram");
	public static final EUInformation KILOPOUND_FORCE = init("M75", 5060405, "kip", "kilopound-force");
	public static final EUInformation KILOPOUND_PER_HOUR = init("M90", 5060912, "klb/h", "kilopound per hour");
	public static final EUInformation KILOROENTGEN = init("KR", 19282, "kR", "kiloroentgen");
	public static final EUInformation KILOSECOND = init("B52", 4338994, "ks", "kilosecond");
	public static final EUInformation KILOSEGMENT = init("KJ", 19274, "", "kilosegment");
	public static final EUInformation KILOSIEMENS = init("B53", 4338995, "kS", "kilosiemens");
	public static final EUInformation KILOSIEMENS_PER_METRE = init("B54", 4338996, "kS/m", "kilosiemens per metre");
	public static final EUInformation KILOTESLA = init("P13", 5255475, "kT", "kilotesla");
	public static final EUInformation KILOTONNE = init("KTN", 4936782, "kt", "kilotonne");
	public static final EUInformation KILOVAR = init("KVR", 4937298, "kvar", "kilovar");
	public static final EUInformation KILOVOLT = init("KVT", 4937300, "kV", "kilovolt");
	public static final EUInformation KILOVOLT_AMPERE_HOUR = init("C79", 4405049, "kVAh", "kilovolt ampere hour");
	public static final EUInformation KILOVOLT_AMPERE_REACTIVE_DEMAND = init("K2", 19250, "",
			"kilovolt ampere reactive demand");
	public static final EUInformation KILOVOLT_AMPERE_REACTIVE_HOUR = init("K3", 19251, "kvar\u00B7h",
			"kilovolt ampere reactive hour");
	public static final EUInformation KILOVOLT_PER_METRE = init("B55", 4338997, "kV/m", "kilovolt per metre");
	public static final EUInformation KILOVOLT___AMPERE = init("KVA", 4937281, "kV\u00B7A", "kilovolt - ampere");
	public static final EUInformation KILOWATT = init("KWT", 4937556, "kW", "kilowatt");
	public static final EUInformation KILOWATT_DEMAND = init("K1", 19249, "", "kilowatt demand");
	public static final EUInformation KILOWATT_HOUR = init("KWH", 4937544, "kW\u00B7h", "kilowatt hour");
	public static final EUInformation KILOWATT_HOUR_PER_CUBIC_METRE = init("E46", 4535350, "kW\u00B7h/m\u00B3",
			"kilowatt hour per cubic metre");
	public static final EUInformation KILOWATT_HOUR_PER_HOUR = init("D03", 4468787, "kW\u00B7h/h",
			"kilowatt hour per hour");
	public static final EUInformation KILOWATT_HOUR_PER_KELVIN = init("E47", 4535351, "kW\u00B7h/K",
			"kilowatt hour per kelvin");
	public static final EUInformation KILOWATT_PER_METRE_DEGREE_CELSIUS = init("N82", 5126194, "kW/(m\u00B7\u00B0C)",
			"kilowatt per metre degree Celsius");
	public static final EUInformation KILOWATT_PER_METRE_KELVIN = init("N81", 5126193, "kW/(m\u00B7K)",
			"kilowatt per metre kelvin");
	public static final EUInformation KILOWATT_PER_SQUARE_METRE_KELVIN = init("N78", 5125944, "kW/(m\u00B2\u00B7K)",
			"kilowatt per square metre kelvin");
	public static final EUInformation KILOWEBER = init("P11", 5255473, "kWb", "kiloweber");
	public static final EUInformation KILOWEBER_PER_METRE = init("B56", 4338998, "kWb/m", "kiloweber per metre");
	public static final EUInformation KIP_PER_SQUARE_INCH = init("N20", 5124656, "ksi", "kip per square inch");
	public static final EUInformation KIT = init("KT", 19284, "", "kit");
	public static final EUInformation KNOT = init("KNT", 4935252, "kn", "knot");
	public static final EUInformation LABOUR_HOUR = init("LH", 19528, "", "labour hour");
	public static final EUInformation LACTIC_DRY_MATERIAL_PERCENTAGE = init("KLK", 4934731, "",
			"lactic dry material percentage");
	public static final EUInformation LACTOSE_EXCESS_PERCENTAGE = init("LAC", 4997443, "", "lactose excess percentage");
	public static final EUInformation LAMBERT = init("P30", 5255984, "Lb", "lambert");
	public static final EUInformation LANGLEY = init("P40", 5256240, "Ly", "langley");
	public static final EUInformation LAYER = init("LR", 19538, "", "layer");
	public static final EUInformation LEAF = init("LEF", 4998470, "", "leaf");
	public static final EUInformation LENGTH = init("LN", 19534, "", "length");
	public static final EUInformation LIGHT_YEAR = init("B57", 4338999, "ly", "light year");
	public static final EUInformation LINEAR_FOOT = init("LF", 19526, "", "linear foot");
	public static final EUInformation LINEAR_METRE = init("LM", 19533, "", "linear metre");
	public static final EUInformation LINEAR_YARD = init("LY", 19545, "", "linear yard");
	public static final EUInformation LINK = init("LK", 19531, "", "link");
	public static final EUInformation LIQUID_PINT_US = init("PTL", 5264460, "liq pt (US)", "liquid pint (US)");
	public static final EUInformation LIQUID_POUND = init("LP", 19536, "", "liquid pound");
	public static final EUInformation LIQUID_QUART_US = init("QTL", 5329996, "liq qt (US)", "liquid quart (US)");
	public static final EUInformation LITRE = init("LTR", 5002322, "l", "litre");
	public static final EUInformation LITRE_OF_PURE_ALCOHOL = init("LPA", 5001281, "", "litre of pure alcohol");
	public static final EUInformation LITRE_PER_BAR = init("G95", 4667701, "l/bar", "litre per bar");
	public static final EUInformation LITRE_PER_DAY = init("LD", 19524, "l/d", "litre per day");
	public static final EUInformation LITRE_PER_DAY_BAR = init("G82", 4667442, "l/(d\u00B7bar)", "litre per day bar");
	public static final EUInformation LITRE_PER_DAY_KELVIN = init("G65", 4666933, "l/(d\u00B7K)",
			"litre per day kelvin");
	public static final EUInformation LITRE_PER_HOUR = init("E32", 4535090, "l/h", "litre per hour");
	public static final EUInformation LITRE_PER_HOUR_BAR = init("G83", 4667443, "l/(h\u00B7bar)", "litre per hour bar");
	public static final EUInformation LITRE_PER_HOUR_KELVIN = init("G66", 4666934, "l/(h\u00B7K)",
			"litre per hour kelvin");
	public static final EUInformation LITRE_PER_KELVIN = init("G28", 4665912, "l/K", "litre per kelvin");
	public static final EUInformation LITRE_PER_KILOGRAM = init("H83", 4732979, "l/kg", "litre per kilogram");
	public static final EUInformation LITRE_PER_LITRE = init("K62", 4929074, "l/l", "litre per litre");
	public static final EUInformation LITRE_PER_MINUTE = init("L2", 19506, "l/min", "litre per minute");
	public static final EUInformation LITRE_PER_MINUTE_BAR = init("G84", 4667444, "l/(min\u00B7bar)",
			"litre per minute bar");
	public static final EUInformation LITRE_PER_MINUTE_KELVIN = init("G67", 4666935, "l/(min\u00B7K)",
			"litre per minute kelvin");
	public static final EUInformation LITRE_PER_MOLE = init("B58", 4339000, "l/mol", "litre per mole");
	public static final EUInformation LITRE_PER_SECOND = init("G51", 4666673, "l/s", "litre per second");
	public static final EUInformation LITRE_PER_SECOND_BAR = init("G85", 4667445, "l/(s\u00B7bar)",
			"litre per second bar");
	public static final EUInformation LITRE_PER_SECOND_KELVIN = init("G68", 4666936, "l/(s\u00B7K)",
			"litre per second kelvin");
	public static final EUInformation LOAD = init("NL", 20044, "", "load");
	public static final EUInformation LOT_UNIT_OF_PROCUREMENT = init("LO", 19535, "", "lot  [unit of procurement]");
	public static final EUInformation LOT_UNIT_OF_WEIGHT = init("D04", 4468788, "", "lot  [unit of weight]");
	public static final EUInformation LUMEN = init("LUM", 5002573, "lm", "lumen");
	public static final EUInformation LUMEN_HOUR = init("B59", 4339001, "lm\u00B7h", "lumen hour");
	public static final EUInformation LUMEN_PER_SQUARE_FOOT = init("P25", 5255733, "lm/ft\u00B2",
			"lumen per square foot");
	public static final EUInformation LUMEN_PER_SQUARE_METRE = init("B60", 4339248, "lm/m\u00B2",
			"lumen per square metre");
	public static final EUInformation LUMEN_PER_WATT = init("B61", 4339249, "lm/W", "lumen per watt");
	public static final EUInformation LUMEN_SECOND = init("B62", 4339250, "lm\u00B7s", "lumen second");
	public static final EUInformation LUMP_SUM = init("LS", 19539, "", "lump sum");
	public static final EUInformation LUX = init("LUX", 5002584, "lx", "lux");
	public static final EUInformation LUX_HOUR = init("B63", 4339251, "lx\u00B7h", "lux hour");
	public static final EUInformation LUX_SECOND = init("B64", 4339252, "lx\u00B7s", "lux second");
	public static final EUInformation MANMONTH = init("3C", 13123, "", "manmonth");
	public static final EUInformation MEAL = init("Q3", 20787, "", "meal");
	public static final EUInformation MEBIBIT = init("D11", 4469041, "Mibit", "mebibit");
	public static final EUInformation MEBIBIT_PER_CUBIC_METRE = init("E77", 4536119, "Mibit/m\u00B3",
			"mebibit per cubic metre");
	public static final EUInformation MEBIBIT_PER_METRE = init("E75", 4536117, "Mibit/m", "mebibit per metre");
	public static final EUInformation MEBIBIT_PER_SQUARE_METRE = init("E76", 4536118, "Mibit/m\u00B2",
			"mebibit per square metre");
	public static final EUInformation MEBIBYTE = init("E63", 4535859, "Mibyte", "mebibyte");
	public static final EUInformation MEGAAMPERE = init("H38", 4731704, "MA", "megaampere");
	public static final EUInformation MEGAAMPERE_PER_SQUARE_METRE = init("B66", 4339254, "MA/m\u00B2",
			"megaampere per square metre");
	public static final EUInformation MEGABAUD = init("J54", 4863284, "MBd", "megabaud");
	public static final EUInformation MEGABECQUEREL = init("4N", 13390, "MBq", "megabecquerel");
	public static final EUInformation MEGABECQUEREL_PER_KILOGRAM = init("B67", 4339255, "MBq/kg",
			"megabecquerel per kilogram");
	public static final EUInformation MEGABIT = init("D36", 4469558, "Mbit", "megabit");
	public static final EUInformation MEGABIT_PER_SECOND = init("E20", 4534832, "Mbit/s", "megabit per second");
	public static final EUInformation MEGABYTE = init("4L", 13388, "Mbyte", "megabyte");
	public static final EUInformation MEGABYTE_PER_SECOND = init("P95", 5257525, "Mbyte/s", "megabyte per second");
	public static final EUInformation MEGACOULOMB = init("D77", 4470583, "MC", "megacoulomb");
	public static final EUInformation MEGACOULOMB_PER_CUBIC_METRE = init("B69", 4339257, "MC/m\u00B3",
			"megacoulomb per cubic metre");
	public static final EUInformation MEGACOULOMB_PER_SQUARE_METRE = init("B70", 4339504, "MC/m\u00B2",
			"megacoulomb per square metre");
	public static final EUInformation MEGAELECTRONVOLT = init("B71", 4339505, "MeV", "megaelectronvolt");
	public static final EUInformation MEGAGRAM = init("2U", 12885, "Mg", "megagram");
	public static final EUInformation MEGAGRAM_PER_CUBIC_METRE = init("B72", 4339506, "Mg/m\u00B3",
			"megagram per cubic metre");
	public static final EUInformation MEGAHERTZ = init("MHZ", 5064794, "MHz", "megahertz");
	public static final EUInformation MEGAHERTZ_KILOMETRE = init("H39", 4731705, "MHz\u00B7km", "megahertz kilometre");
	public static final EUInformation MEGAHERTZ_METRE = init("M27", 5059127, "MHz\u00B7m", "megahertz metre");
	public static final EUInformation MEGAJOULE = init("3B", 13122, "MJ", "megajoule");
	public static final EUInformation MEGAJOULE_PER_CUBIC_METRE = init("JM", 19021, "MJ/m\u00B3",
			"megajoule per cubic metre");
	public static final EUInformation MEGAJOULE_PER_KILOGRAM = init("JK", 19019, "MJ/kg", "megajoule per kilogram");
	public static final EUInformation MEGAJOULE_PER_SECOND = init("D78", 4470584, "MJ/s", "megajoule per second");
	public static final EUInformation MEGALITRE = init("MAL", 5062988, "Ml", "megalitre");
	public static final EUInformation MEGAMETRE = init("MAM", 5062989, "Mm", "megametre");
	public static final EUInformation MEGANEWTON = init("B73", 4339507, "MN", "meganewton");
	public static final EUInformation MEGANEWTON_METRE = init("B74", 4339508, "MN\u00B7m", "meganewton metre");
	public static final EUInformation MEGAOHM = init("B75", 4339509, "M\u2126", "megaohm");
	public static final EUInformation MEGAOHM_KILOMETRE = init("H88", 4732984, "M\u2126\u00B7km", "megaohm kilometre");
	public static final EUInformation MEGAOHM_METRE = init("B76", 4339510, "M\u2126\u00B7m", "megaohm metre");
	public static final EUInformation MEGAOHM_PER_KILOMETRE = init("H36", 4731702, "M\u2126/km",
			"megaohm per kilometre");
	public static final EUInformation MEGAOHM_PER_METRE = init("H37", 4731703, "M\u2126/m", "megaohm per metre");
	public static final EUInformation MEGAPASCAL = init("MPA", 5066817, "MPa", "megapascal");
	public static final EUInformation MEGAPASCAL_CUBIC_METRE_PER_SECOND = init("F98", 4602168, "MPa\u00B7m\u00B3/s",
			"megapascal cubic metre per second");
	public static final EUInformation MEGAPASCAL_LITRE_PER_SECOND = init("F97", 4602167, "MPa\u00B7l/s",
			"megapascal litre per second");
	public static final EUInformation MEGAPASCAL_PER_BAR = init("F05", 4599861, "MPa/bar", "megapascal per bar");
	public static final EUInformation MEGAPASCAL_PER_KELVIN = init("F85", 4601909, "MPa/K", "megapascal per kelvin");
	public static final EUInformation MEGAPIXEL = init("E38", 4535096, "", "megapixel");
	public static final EUInformation MEGASIEMENS_PER_METRE = init("B77", 4339511, "MS/m", "megasiemens per metre");
	public static final EUInformation MEGAVAR = init("MAR", 5062994, "Mvar", "megavar");
	public static final EUInformation MEGAVOLT = init("B78", 4339512, "MV", "megavolt");
	public static final EUInformation MEGAVOLT_AMPERE_REACTIVE_HOUR = init("MAH", 5062984, "Mvar\u00B7h",
			"megavolt ampere reactive hour");
	public static final EUInformation MEGAVOLT_PER_METRE = init("B79", 4339513, "MV/m", "megavolt per metre");
	public static final EUInformation MEGAVOLT___AMPERE = init("MVA", 5068353, "MV\u00B7A", "megavolt - ampere");
	public static final EUInformation MEGAWATT = init("MAW", 5062999, "MW", "megawatt");
	public static final EUInformation MEGAWATTS_PER_MINUTE = init("Q35", 5321525, "MW/min", "megawatts per minute");
	public static final EUInformation MEGAWATT_HOUR_1000_KWH = init("MWH", 5068616, "MW\u00B7h",
			"megawatt hour (1000\u00A0kW.h)");
	public static final EUInformation MEGAWATT_HOUR_PER_HOUR = init("E07", 4534327, "MW\u00B7h/h",
			"megawatt hour per hour");
	public static final EUInformation MEGAWATT_PER_HERTZ = init("E08", 4534328, "MW/Hz", "megawatt per hertz");
	public static final EUInformation MESH = init("57", 13623, "", "mesh");
	public static final EUInformation MESSAGE = init("NF", 20038, "", "message");
	public static final EUInformation METRE = init("MTR", 5067858, "m", "metre");
	public static final EUInformation METRE_KELVIN = init("D18", 4469048, "m\u00B7K", "metre kelvin");
	public static final EUInformation METRE_PER_BAR = init("G05", 4665397, "m/bar", "metre per bar");
	public static final EUInformation METRE_PER_DEGREE_CELCIUS_METRE = init("N83", 5126195, "m/(\u00B0C\u00B7m)",
			"metre per degree Celcius metre");
	public static final EUInformation METRE_PER_HOUR = init("M60", 5060144, "m/h", "metre per hour");
	public static final EUInformation METRE_PER_KELVIN = init("F52", 4601138, "m/K", "metre per kelvin");
	public static final EUInformation METRE_PER_MINUTE = init("2X", 12888, "m/min", "metre per minute");
	public static final EUInformation METRE_PER_PASCAL = init("M53", 5059891, "m/Pa", "metre per pascal");
	public static final EUInformation METRE_PER_RADIANT = init("M55", 5059893, "m/rad", "metre per radiant");
	public static final EUInformation METRE_PER_SECOND = init("MTS", 5067859, "m/s", "metre per second");
	public static final EUInformation METRE_PER_SECOND_BAR = init("L13", 4993331, "(m/s)/bar", "metre per second bar");
	public static final EUInformation METRE_PER_SECOND_KELVIN = init("L12", 4993330, "(m/s)/K",
			"metre per second kelvin");
	public static final EUInformation METRE_PER_SECOND_PASCAL = init("M59", 5059897, "(m/s)/Pa",
			"metre per second pascal");
	public static final EUInformation METRE_PER_SECOND_SQUARED = init("MSK", 5067595, "m/s\u00B2",
			"metre per second squared");
	public static final EUInformation METRE_PER_VOLT_SECOND = init("H58", 4732216, "m/(V\u00B7s)",
			"metre per volt second");
	public static final EUInformation METRE_TO_THE_FOURTH_POWER = init("B83", 4339763, "m\u2074",
			"metre to the fourth power");
	public static final EUInformation METRIC_CARAT = init("CTM", 4412493, "", "metric carat");
	public static final EUInformation METRIC_TON_INCLUDING_CONTAINER = init("TIC", 5523779, "",
			"metric ton, including container");
	public static final EUInformation METRIC_TON_INCLUDING_INNER_PACKAGING = init("TIP", 5523792, "",
			"metric ton, including inner packaging");
	public static final EUInformation METRIC_TON_LUBRICATING_OIL = init("LUB", 5002562, "",
			"metric ton, lubricating oil");
	public static final EUInformation MICROAMPERE = init("B84", 4339764, "\u00B5A", "microampere");
	public static final EUInformation MICROBAR = init("B85", 4339765, "\u00B5bar", "microbar");
	public static final EUInformation MICROBECQUEREL = init("H08", 4730936, "\u00B5Bq", "microbecquerel");
	public static final EUInformation MICROCOULOMB = init("B86", 4339766, "\u00B5C", "microcoulomb");
	public static final EUInformation MICROCOULOMB_PER_CUBIC_METRE = init("B87", 4339767, "\u00B5C/m\u00B3",
			"microcoulomb per cubic metre");
	public static final EUInformation MICROCOULOMB_PER_SQUARE_METRE = init("B88", 4339768, "\u00B5C/m\u00B2",
			"microcoulomb per square metre");
	public static final EUInformation MICROCURIE = init("M5", 19765, "\u00B5Ci", "microcurie");
	public static final EUInformation MICROFARAD = init("4O", 13391, "\u00B5F", "microfarad");
	public static final EUInformation MICROFARAD_PER_KILOMETRE = init("H28", 4731448, "\u00B5F/km",
			"microfarad per kilometre");
	public static final EUInformation MICROFARAD_PER_METRE = init("B89", 4339769, "\u00B5F/m", "microfarad per metre");
	public static final EUInformation MICROGRAM = init("MC", 19779, "\u00B5g", "microgram");
	public static final EUInformation MICROGRAM_PER_CUBIC_METRE = init("GQ", 18257, "\u00B5g/m\u00B3",
			"microgram per cubic metre");
	public static final EUInformation MICROGRAM_PER_CUBIC_METRE_BAR = init("J35", 4862773, "(\u00B5g/m\u00B3)/bar",
			"microgram per cubic metre bar");
	public static final EUInformation MICROGRAM_PER_CUBIC_METRE_KELVIN = init("J34", 4862772, "(\u00B5g/m\u00B3)/K",
			"microgram per cubic metre kelvin");
	public static final EUInformation MICROGRAM_PER_HECTOGRAM = init("Q29", 5321273, "\u00B5g/hg",
			"microgram per hectogram");
	public static final EUInformation MICROGRAM_PER_KILOGRAM = init("J33", 4862771, "\u00B5g/kg",
			"microgram per kilogram");
	public static final EUInformation MICROGRAM_PER_LITRE = init("H29", 4731449, "\u00B5g/l", "microgram per litre");
	public static final EUInformation MICROGRAY_PER_HOUR = init("P63", 5256755, "\u00B5Gy/h", "microgray per hour");
	public static final EUInformation MICROGRAY_PER_MINUTE = init("P59", 5256505, "\u00B5Gy/min",
			"microgray per minute");
	public static final EUInformation MICROGRAY_PER_SECOND = init("P55", 5256501, "\u00B5Gy/s", "microgray per second");
	public static final EUInformation MICROHENRY = init("B90", 4340016, "\u00B5H", "microhenry");
	public static final EUInformation MICROHENRY_PER_KILOOHM = init("G98", 4667704, "\u00B5H/k\u2126",
			"microhenry per kiloohm");
	public static final EUInformation MICROHENRY_PER_METRE = init("B91", 4340017, "\u00B5H/m", "microhenry per metre");
	public static final EUInformation MICROHENRY_PER_OHM = init("G99", 4667705, "\u00B5H/\u2126", "microhenry per ohm");
	public static final EUInformation MICROLITRE = init("4G", 13383, "\u00B5l", "microlitre");
	public static final EUInformation MICROLITRE_PER_LITRE = init("J36", 4862774, "\u00B5l/l", "microlitre per litre");
	public static final EUInformation MICROMETRE_MICRON = init("4H", 13384, "\u00B5m", "micrometre (micron)");
	public static final EUInformation MICROMETRE_PER_KELVIN = init("F50", 4601136, "\u00B5m/K",
			"micrometre per kelvin");
	public static final EUInformation MICROMOLE = init("FH", 17992, "\u00B5mol", "micromole");
	public static final EUInformation MICRONEWTON = init("B92", 4340018, "\u00B5N", "micronewton");
	public static final EUInformation MICRONEWTON_METRE = init("B93", 4340019, "\u00B5N\u00B7m", "micronewton metre");
	public static final EUInformation MICROOHM = init("B94", 4340020, "\u00B5\u2126", "microohm");
	public static final EUInformation MICROOHM_METRE = init("B95", 4340021, "\u00B5\u2126\u00B7m", "microohm metre");
	public static final EUInformation MICROPASCAL = init("B96", 4340022, "\u00B5Pa", "micropascal");
	public static final EUInformation MICROPOISE = init("J32", 4862770, "\u00B5P", "micropoise");
	public static final EUInformation MICRORADIAN = init("B97", 4340023, "\u00B5rad", "microradian");
	public static final EUInformation MICROSECOND = init("B98", 4340024, "\u00B5s", "microsecond");
	public static final EUInformation MICROSIEMENS = init("B99", 4340025, "\u00B5S", "microsiemens");
	public static final EUInformation MICROSIEMENS_PER_CENTIMETRE = init("G42", 4666418, "\u00B5S/cm",
			"microsiemens per centimetre");
	public static final EUInformation MICROSIEMENS_PER_METRE = init("G43", 4666419, "\u00B5S/m",
			"microsiemens per metre");
	public static final EUInformation MICROSIEVERT_PER_HOUR = init("P72", 5257010, "\u00B5Sv/h",
			"microsievert per hour");
	public static final EUInformation MICROSIEVERT_PER_MINUTE = init("P76", 5257014, "\u00B5Sv/min",
			"microsievert per minute");
	public static final EUInformation MICROSIEVERT_PER_SECOND = init("P67", 5256759, "\u00B5Sv/s",
			"microsievert per second");
	public static final EUInformation MICROTESLA = init("D81", 4470833, "\u00B5T", "microtesla");
	public static final EUInformation MICROVOLT = init("D82", 4470834, "\u00B5V", "microvolt");
	public static final EUInformation MICROVOLT_PER_METRE = init("C3", 17203, "\u00B5V/m", "microvolt per metre");
	public static final EUInformation MICROWATT = init("D80", 4470832, "\u00B5W", "microwatt");
	public static final EUInformation MICROWATT_PER_SQUARE_METRE = init("D85", 4470837, "\u00B5W/m\u00B2",
			"microwatt per square metre");
	public static final EUInformation MICRO_INCH = init("M7", 19767, "\u00B5in", "micro-inch");
	public static final EUInformation MIL = init("M43", 5059635, "mil", "mil");
	public static final EUInformation MILE_BASED_ON_US_SURVEY_FOOT = init("M52", 5059890, "mi (US survey)",
			"mile (based on U.S. survey foot)");
	public static final EUInformation MILE_PER_HOUR_STATUTE_MILE = init("HM", 18509, "mile/h",
			"mile per hour (statute mile)");
	public static final EUInformation MILE_PER_MINUTE = init("M57", 5059895, "mi/min", "mile per minute");
	public static final EUInformation MILE_PER_SECOND = init("M58", 5059896, "mi/s", "mile per second");
	public static final EUInformation MILE_STATUTE_MILE = init("SMI", 5459273, "mile", "mile (statute mile)");
	public static final EUInformation MILE_STATUTE_MILE_PER_SECOND_SQUARED = init("M42", 5059634, "mi/s\u00B2",
			"mile (statute mile) per second squared");
	public static final EUInformation MILLE = init("E12", 4534578, "", "mille");
	public static final EUInformation MILLIAMPERE = init("4K", 13387, "mA", "milliampere");
	public static final EUInformation MILLIAMPERE_HOUR = init("E09", 4534329, "mA\u00B7h", "milliampere hour");
	public static final EUInformation MILLIAMPERE_PER_BAR = init("F59", 4601145, "mA/bar", "milliampere per bar");
	public static final EUInformation MILLIAMPERE_PER_INCH = init("F08", 4599864, "mA/in", "milliampere per inch");
	public static final EUInformation MILLIAMPERE_PER_LITRE_MINUTE = init("G59", 4666681, "mA/(l\u00B7min)",
			"milliampere per litre minute");
	public static final EUInformation MILLIAMPERE_PER_MILLIMETRE = init("F76", 4601654, "mA/mm",
			"milliampere per millimetre");
	public static final EUInformation MILLIAMPERE_PER_POUND_FORCE_PER_SQUARE_INCH = init("F57", 4601143,
			"mA/(lbf/in\u00B2)", "milliampere per pound-force per square inch");
	public static final EUInformation MILLIARD = init("MLD", 5065796, "", "milliard");
	public static final EUInformation MILLIBAR = init("MBR", 5063250, "mbar", "millibar");
	public static final EUInformation MILLIBAR_CUBIC_METRE_PER_SECOND = init("F96", 4602166, "mbar\u00B7m\u00B3/s",
			"millibar cubic metre per second");
	public static final EUInformation MILLIBAR_LITRE_PER_SECOND = init("F95", 4602165, "mbar\u00B7l/s",
			"millibar litre per second");
	public static final EUInformation MILLIBAR_PER_BAR = init("F04", 4599860, "mbar/bar", "millibar per bar");
	public static final EUInformation MILLIBAR_PER_KELVIN = init("F84", 4601908, "mbar/K", "millibar per kelvin");
	public static final EUInformation MILLICANDELA = init("P34", 5255988, "mcd", "millicandela");
	public static final EUInformation MILLICOULOMB = init("D86", 4470838, "mC", "millicoulomb");
	public static final EUInformation MILLICOULOMB_PER_CUBIC_METRE = init("D88", 4470840, "mC/m\u00B3",
			"millicoulomb per cubic metre");
	public static final EUInformation MILLICOULOMB_PER_KILOGRAM = init("C8", 17208, "mC/kg",
			"millicoulomb per kilogram");
	public static final EUInformation MILLICOULOMB_PER_SQUARE_METRE = init("D89", 4470841, "mC/m\u00B2",
			"millicoulomb per square metre");
	public static final EUInformation MILLICURIE = init("MCU", 5063509, "mCi", "millicurie");
	public static final EUInformation MILLIEQUIVALENCE_CAUSTIC_POTASH_PER_GRAM_OF_PRODUCT = init("KO", 19279, "",
			"milliequivalence caustic potash per gram of product");
	public static final EUInformation MILLIFARAD = init("C10", 4403504, "mF", "millifarad");
	public static final EUInformation MILLIGAL = init("C11", 4403505, "mGal", "milligal");
	public static final EUInformation MILLIGRAM = init("MGM", 5064525, "mg", "milligram");
	public static final EUInformation MILLIGRAM_PER_BAR = init("F75", 4601653, "mg/bar", "milligram per bar");
	public static final EUInformation MILLIGRAM_PER_CUBIC_METRE = init("GP", 18256, "mg/m\u00B3",
			"milligram per cubic metre");
	public static final EUInformation MILLIGRAM_PER_CUBIC_METRE_BAR = init("L18", 4993336, "(mg/m\u00B3)/bar",
			"milligram per cubic metre bar");
	public static final EUInformation MILLIGRAM_PER_CUBIC_METRE_KELVIN = init("L17", 4993335, "(mg/m\u00B3)/K",
			"milligram per cubic metre kelvin");
	public static final EUInformation MILLIGRAM_PER_DAY = init("F32", 4600626, "mg/d", "milligram per day");
	public static final EUInformation MILLIGRAM_PER_DAY_BAR = init("F70", 4601648, "mg/(d\u00B7bar)",
			"milligram per day bar");
	public static final EUInformation MILLIGRAM_PER_DAY_KELVIN = init("F43", 4600883, "mg/(d\u00B7K)",
			"milligram per day kelvin");
	public static final EUInformation MILLIGRAM_PER_GRAM = init("H64", 4732468, "mg/g", "milligram per gram");
	public static final EUInformation MILLIGRAM_PER_HOUR = init("4M", 13389, "mg/h", "milligram per hour");
	public static final EUInformation MILLIGRAM_PER_HOUR_BAR = init("F71", 4601649, "mg/(h\u00B7bar)",
			"milligram per hour bar");
	public static final EUInformation MILLIGRAM_PER_HOUR_KELVIN = init("F44", 4600884, "mg/(h\u00B7K)",
			"milligram per hour kelvin");
	public static final EUInformation MILLIGRAM_PER_KELVIN = init("F16", 4600118, "mg/K", "milligram per kelvin");
	public static final EUInformation MILLIGRAM_PER_KILOGRAM = init("NA", 20033, "mg/kg", "milligram per kilogram");
	public static final EUInformation MILLIGRAM_PER_LITRE = init("M1", 19761, "mg/l", "milligram per litre");
	public static final EUInformation MILLIGRAM_PER_METRE = init("C12", 4403506, "mg/m", "milligram per metre");
	public static final EUInformation MILLIGRAM_PER_MINUTE = init("F33", 4600627, "mg/min", "milligram per minute");
	public static final EUInformation MILLIGRAM_PER_MINUTE_BAR = init("F72", 4601650, "mg/(min\u00B7bar)",
			"milligram per minute bar");
	public static final EUInformation MILLIGRAM_PER_MINUTE_KELVIN = init("F45", 4600885, "mg/(min\u00B7K)",
			"milligram per minute kelvin");
	public static final EUInformation MILLIGRAM_PER_SECOND = init("F34", 4600628, "mg/s", "milligram per second");
	public static final EUInformation MILLIGRAM_PER_SECOND_BAR = init("F73", 4601651, "mg/(s\u00B7bar)",
			"milligram per second bar");
	public static final EUInformation MILLIGRAM_PER_SECOND_KELVIN = init("F46", 4600886, "mg/(s\u00B7K)",
			"milligram per second kelvin");
	public static final EUInformation MILLIGRAM_PER_SQUARE_CENTIMETRE = init("H63", 4732467, "mg/cm\u00B2",
			"milligram per square centimetre");
	public static final EUInformation MILLIGRAM_PER_SQUARE_METRE = init("GO", 18255, "mg/m\u00B2",
			"milligram per square metre");
	public static final EUInformation MILLIGRAY = init("C13", 4403507, "mGy", "milligray");
	public static final EUInformation MILLIGRAY_PER_HOUR = init("P62", 5256754, "mGy/h", "milligray per hour");
	public static final EUInformation MILLIGRAY_PER_MINUTE = init("P58", 5256504, "mGy/min", "milligray per minute");
	public static final EUInformation MILLIGRAY_PER_SECOND = init("P54", 5256500, "mGy/s", "milligray per second");
	public static final EUInformation MILLIHENRY = init("C14", 4403508, "mH", "millihenry");
	public static final EUInformation MILLIHENRY_PER_KILOOHM = init("H05", 4730933, "mH/k\u2126",
			"millihenry per kiloohm");
	public static final EUInformation MILLIHENRY_PER_OHM = init("H06", 4730934, "mH/\u2126", "millihenry per ohm");
	public static final EUInformation MILLIJOULE = init("C15", 4403509, "mJ", "millijoule");
	public static final EUInformation MILLILITRE = init("MLT", 5065812, "ml", "millilitre");
	public static final EUInformation MILLILITRE_PER_BAR = init("G97", 4667703, "ml/bar", "millilitre per bar");
	public static final EUInformation MILLILITRE_PER_CUBIC_METRE = init("H65", 4732469, "ml/m\u00B3",
			"millilitre per cubic metre");
	public static final EUInformation MILLILITRE_PER_DAY = init("G54", 4666676, "ml/d", "millilitre per day");
	public static final EUInformation MILLILITRE_PER_DAY_BAR = init("G90", 4667696, "ml/(d\u00B7bar)",
			"millilitre per day bar");
	public static final EUInformation MILLILITRE_PER_DAY_KELVIN = init("G73", 4667187, "ml/(d\u00B7K)",
			"millilitre per day kelvin");
	public static final EUInformation MILLILITRE_PER_HOUR = init("G55", 4666677, "ml/h", "millilitre per hour");
	public static final EUInformation MILLILITRE_PER_HOUR_BAR = init("G91", 4667697, "ml/(h\u00B7bar)",
			"millilitre per hour bar");
	public static final EUInformation MILLILITRE_PER_HOUR_KELVIN = init("G74", 4667188, "ml/(h\u00B7K)",
			"millilitre per hour kelvin");
	public static final EUInformation MILLILITRE_PER_KELVIN = init("G30", 4666160, "ml/K", "millilitre per kelvin");
	public static final EUInformation MILLILITRE_PER_KILOGRAM = init("KX", 19288, "ml/kg", "millilitre per kilogram");
	public static final EUInformation MILLILITRE_PER_LITRE = init("L19", 4993337, "ml/l", "millilitre per litre");
	public static final EUInformation MILLILITRE_PER_MINUTE = init("41", 13361, "ml/min", "millilitre per minute");
	public static final EUInformation MILLILITRE_PER_MINUTE_BAR = init("G92", 4667698, "ml/(min\u00B7bar)",
			"millilitre per minute bar");
	public static final EUInformation MILLILITRE_PER_MINUTE_KELVIN = init("G75", 4667189, "ml/(min\u00B7K)",
			"millilitre per minute kelvin");
	public static final EUInformation MILLILITRE_PER_SECOND = init("40", 13360, "ml/s", "millilitre per second");
	public static final EUInformation MILLILITRE_PER_SECOND_BAR = init("G93", 4667699, "ml/(s\u00B7bar)",
			"millilitre per second bar");
	public static final EUInformation MILLILITRE_PER_SECOND_KELVIN = init("G76", 4667190, "ml/(s\u00B7K)",
			"millilitre per second kelvin");
	public static final EUInformation MILLILITRE_PER_SQUARE_CENTIMETRE_MINUTE = init("M22", 5059122,
			"(ml/min)/cm\u00B2", "millilitre per square centimetre minute");
	public static final EUInformation MILLILITRE_PER_SQUARE_CENTIMETRE_SECOND = init("35", 13109,
			"ml/(cm\u00B2\u00B7s)", "millilitre per square centimetre second");
	public static final EUInformation MILLIMETRE = init("MMT", 5066068, "mm", "millimetre");
	public static final EUInformation MILLIMETRE_PER_BAR = init("G06", 4665398, "mm/bar", "millimetre per bar");
	public static final EUInformation MILLIMETRE_PER_DEGREE_CELCIUS_METRE = init("E97", 4536631, "mm/(\u00B0C\u00B7m)",
			"millimetre per degree Celcius metre");
	public static final EUInformation MILLIMETRE_PER_HOUR = init("H67", 4732471, "mm/h", "millimetre per hour");
	public static final EUInformation MILLIMETRE_PER_KELVIN = init("F53", 4601139, "mm/K", "millimetre per kelvin");
	public static final EUInformation MILLIMETRE_PER_MINUTE = init("H81", 4732977, "mm/min", "millimetre per minute");
	public static final EUInformation MILLIMETRE_PER_SECOND = init("C16", 4403510, "mm/s", "millimetre per second");
	public static final EUInformation MILLIMETRE_PER_SECOND_SQUARED = init("M41", 5059633, "mm/s\u00B2",
			"millimetre per second squared");
	public static final EUInformation MILLIMETRE_PER_YEAR = init("H66", 4732470, "mm/y", "millimetre per year");
	public static final EUInformation MILLIMETRE_SQUARED_PER_SECOND = init("C17", 4403511, "mm\u00B2/s",
			"millimetre squared per second");
	public static final EUInformation MILLIMETRE_TO_THE_FOURTH_POWER = init("G77", 4667191, "mm\u2074",
			"millimetre to the fourth power");
	public static final EUInformation MILLIMOLE = init("C18", 4403512, "mmol", "millimole");
	public static final EUInformation MILLIMOLE_PER_GRAM = init("H68", 4732472, "mmol/g", "millimole per gram");
	public static final EUInformation MILLIMOLE_PER_KILOGRAM = init("D87", 4470839, "mmol/kg",
			"millimole per kilogram");
	public static final EUInformation MILLIMOLE_PER_LITRE = init("M33", 5059379, "mmol/l", "millimole per litre");
	public static final EUInformation MILLINEWTON = init("C20", 4403760, "mN", "millinewton");
	public static final EUInformation MILLINEWTON_METRE = init("D83", 4470835, "mN\u00B7m", "millinewton metre");
	public static final EUInformation MILLINEWTON_PER_METRE = init("C22", 4403762, "mN/m", "millinewton per metre");
	public static final EUInformation MILLIOHM = init("E45", 4535349, "m\u2126", "milliohm");
	public static final EUInformation MILLIOHM_METRE = init("C23", 4403763, "m\u2126\u00B7m", "milliohm metre");
	public static final EUInformation MILLIOHM_PER_METRE = init("F54", 4601140, "m\u2126/m", "milliohm per metre");
	public static final EUInformation MILLION = init("MIO", 5065039, "", "million");
	public static final EUInformation MILLION_BTUIT_PER_HOUR = init("E16", 4534582, "BtuIT/h",
			"million Btu(IT) per hour");
	public static final EUInformation MILLION_BTU_PER_1000_CUBIC_FOOT = init("M9", 19769, "MBTU/kft\u00B3",
			"million Btu per 1000 cubic foot");
	public static final EUInformation MILLION_CUBIC_METRE = init("HMQ", 4738385, "Mm\u00B3", "million cubic metre");
	public static final EUInformation MILLION_INTERNATIONAL_UNIT = init("MIU", 5065045, "",
			"million international unit");
	public static final EUInformation MILLIPASCAL = init("74", 14132, "mPa", "millipascal");
	public static final EUInformation MILLIPASCAL_PER_METRE = init("P80", 5257264, "mPa/m", "millipascal per metre");
	public static final EUInformation MILLIPASCAL_SECOND = init("C24", 4403764, "mPa\u00B7s", "millipascal second");
	public static final EUInformation MILLIPASCAL_SECOND_PER_BAR = init("L16", 4993334, "mPa\u00B7s/bar",
			"millipascal second per bar");
	public static final EUInformation MILLIPASCAL_SECOND_PER_KELVIN = init("L15", 4993333, "mPa\u00B7s/K",
			"millipascal second per kelvin");
	public static final EUInformation MILLIRADIAN = init("C25", 4403765, "mrad", "milliradian");
	public static final EUInformation MILLIROENTGEN = init("2Y", 12889, "mR", "milliroentgen");
	public static final EUInformation MILLIROENTGEN_AEQUIVALENT_MEN = init("L31", 4993841, "mrem",
			"milliroentgen aequivalent men");
	public static final EUInformation MILLISECOND = init("C26", 4403766, "ms", "millisecond");
	public static final EUInformation MILLISIEMENS = init("C27", 4403767, "mS", "millisiemens");
	public static final EUInformation MILLISIEMENS_PER_CENTIMETRE = init("H61", 4732465, "mS/cm",
			"millisiemens per centimetre");
	public static final EUInformation MILLISIEVERT = init("C28", 4403768, "mSv", "millisievert");
	public static final EUInformation MILLISIEVERT_PER_HOUR = init("P71", 5257009, "mSv/h", "millisievert per hour");
	public static final EUInformation MILLISIEVERT_PER_MINUTE = init("P75", 5257013, "mSv/min",
			"millisievert per minute");
	public static final EUInformation MILLISIEVERT_PER_SECOND = init("P66", 5256758, "mSv/s",
			"millisievert per second");
	public static final EUInformation MILLITESLA = init("C29", 4403769, "mT", "millitesla");
	public static final EUInformation MILLIVOLT = init("2Z", 12890, "mV", "millivolt");
	public static final EUInformation MILLIVOLT_PER_KELVIN = init("D49", 4469817, "mV/K", "millivolt per kelvin");
	public static final EUInformation MILLIVOLT_PER_METRE = init("C30", 4404016, "mV/m", "millivolt per metre");
	public static final EUInformation MILLIVOLT_PER_MINUTE = init("H62", 4732466, "mV/min", "millivolt per minute");
	public static final EUInformation MILLIVOLT___AMPERE = init("M35", 5059381, "mV\u00B7A", "millivolt - ampere");
	public static final EUInformation MILLIWATT = init("C31", 4404017, "mW", "milliwatt");
	public static final EUInformation MILLIWATT_PER_SQUARE_METRE = init("C32", 4404018, "mW/m\u00B2",
			"milliwatt per square metre");
	public static final EUInformation MILLIWEBER = init("C33", 4404019, "mWb", "milliweber");
	public static final EUInformation MILLI_INCH = init("77", 14135, "mil", "milli-inch");
	public static final EUInformation MINUTE_UNIT_OF_ANGLE = init("D61", 4470321, "'", "minute [unit of angle]");
	public static final EUInformation MINUTE_UNIT_OF_TIME = init("MIN", 5065038, "min", "minute [unit of time]");
	public static final EUInformation MMSCF_PER_DAY = init("5E", 13637, "", "MMSCF/day");
	public static final EUInformation MODULE_WIDTH = init("H77", 4732727, "MW", "module width");
	public static final EUInformation MOLE = init("C34", 4404020, "mol", "mole");
	public static final EUInformation MOLE_PER_CUBIC_DECIMETRE = init("C35", 4404021, "mol/dm\u00B3",
			"mole per cubic decimetre");
	public static final EUInformation MOLE_PER_CUBIC_METRE = init("C36", 4404022, "mol/m\u00B3",
			"mole per cubic metre");
	public static final EUInformation MOLE_PER_CUBIC_METRE_BAR = init("L29", 4993593, "(mol/m\u00B3)/bar",
			"mole per cubic metre bar");
	public static final EUInformation MOLE_PER_CUBIC_METRE_KELVIN = init("L28", 4993592, "(mol/m\u00B3)/K",
			"mole per cubic metre kelvin");
	public static final EUInformation MOLE_PER_CUBIV_METRE_TO_THE_POWER_SUM_OF_STOICHIOMETRIC_NUMBERS = init("P99",
			5257529, "(mol/m\u00B3)\u2211\u03BDB", "mole per cubiv metre to the power sum of stoichiometric numbers");
	public static final EUInformation MOLE_PER_HOUR = init("L23", 4993587, "mol/h", "mole per hour");
	public static final EUInformation MOLE_PER_KILOGRAM = init("C19", 4403513, "mol/kg", "mole per kilogram");
	public static final EUInformation MOLE_PER_KILOGRAM_BAR = init("L25", 4993589, "(mol/kg)/bar",
			"mole per kilogram bar");
	public static final EUInformation MOLE_PER_KILOGRAM_KELVIN = init("L24", 4993588, "(mol/kg)/K",
			"mole per kilogram kelvin");
	public static final EUInformation MOLE_PER_LITRE = init("C38", 4404024, "mol/l", "mole per litre");
	public static final EUInformation MOLE_PER_LITRE_BAR = init("L27", 4993591, "(mol/l)/bar", "mole per litre bar");
	public static final EUInformation MOLE_PER_LITRE_KELVIN = init("L26", 4993590, "(mol/l)/K",
			"mole per litre kelvin");
	public static final EUInformation MOLE_PER_MINUTE = init("L30", 4993840, "mol/min", "mole per minute");
	public static final EUInformation MOLE_PER_SECOND = init("E95", 4536629, "mol/s", "mole per second");
	public static final EUInformation MOL_PER_CUBIC_METRE_PASCAL = init("P52", 5256498, "(mol/m\u00B3)/Pa",
			"mol per cubic metre pascal");
	public static final EUInformation MOL_PER_KILOGRAM_PASCAL = init("P51", 5256497, "(mol/kg)/Pa",
			"mol per kilogram pascal");
	public static final EUInformation MONETARY_VALUE = init("M4", 19764, "", "monetary value");
	public static final EUInformation MONTH = init("MON", 5066574, "mo", "month");
	public static final EUInformation MUTUALLY_DEFINED = init("ZZ", 23130, "", "mutually defined");
	public static final EUInformation NANOAMPERE = init("C39", 4404025, "nA", "nanoampere");
	public static final EUInformation NANOCOULOMB = init("C40", 4404272, "nC", "nanocoulomb");
	public static final EUInformation NANOFARAD = init("C41", 4404273, "nF", "nanofarad");
	public static final EUInformation NANOFARAD_PER_METRE = init("C42", 4404274, "nF/m", "nanofarad per metre");
	public static final EUInformation NANOGRAM_PER_KILOGRAM = init("L32", 4993842, "ng/kg", "nanogram per kilogram");
	public static final EUInformation NANOGRAY_PER_HOUR = init("P64", 5256756, "nGy/h", "nanogray per hour");
	public static final EUInformation NANOGRAY_PER_MINUTE = init("P60", 5256752, "nGy/min", "nanogray per minute");
	public static final EUInformation NANOGRAY_PER_SECOND = init("P56", 5256502, "nGy/s", "nanogray per second");
	public static final EUInformation NANOHENRY = init("C43", 4404275, "nH", "nanohenry");
	public static final EUInformation NANOHENRY_PER_METRE = init("C44", 4404276, "nH/m", "nanohenry per metre");
	public static final EUInformation NANOLITRE = init("Q34", 5321524, "nl", "nanolitre");
	public static final EUInformation NANOMETRE = init("C45", 4404277, "nm", "nanometre");
	public static final EUInformation NANOOHM = init("P22", 5255730, "n\u2126", "nanoohm");
	public static final EUInformation NANOOHM_METRE = init("C46", 4404278, "n\u2126\u00B7m", "nanoohm metre");
	public static final EUInformation NANOSECOND = init("C47", 4404279, "ns", "nanosecond");
	public static final EUInformation NANOSIEMENS_PER_CENTIMETRE = init("G44", 4666420, "nS/cm",
			"nanosiemens per centimetre");
	public static final EUInformation NANOSIEMENS_PER_METRE = init("G45", 4666421, "nS/m", "nanosiemens per metre");
	public static final EUInformation NANOSIEVERT_PER_HOUR = init("P73", 5257011, "nSv/h", "nanosievert per hour");
	public static final EUInformation NANOSIEVERT_PER_MINUTE = init("P77", 5257015, "nSv/min",
			"nanosievert per minute");
	public static final EUInformation NANOSIEVERT_PER_SECOND = init("P68", 5256760, "nSv/s", "nanosievert per second");
	public static final EUInformation NANOTESLA = init("C48", 4404280, "nT", "nanotesla");
	public static final EUInformation NANOWATT = init("C49", 4404281, "nW", "nanowatt");
	public static final EUInformation NATURAL_UNIT_OF_INFORMATION = init("Q16", 5321014, "nat",
			"natural unit of information");
	public static final EUInformation NATURAL_UNIT_OF_INFORMATION_PER_SECOND = init("Q19", 5321017, "nat/s",
			"natural unit of information per second");
	public static final EUInformation NAUTICAL_MILE = init("NMI", 5131593, "n mile", "nautical mile");
	public static final EUInformation NEPER = init("C50", 4404528, "Np", "neper");
	public static final EUInformation NEPER_PER_SECOND = init("C51", 4404529, "Np/s", "neper per second");
	public static final EUInformation NET_KILOGRAM = init("58", 13624, "", "net kilogram");
	public static final EUInformation NET_TON = init("NT", 20052, "", "net ton");
	public static final EUInformation NEWTON = init("NEW", 5129559, "N", "newton");
	public static final EUInformation NEWTON_CENTIMETRE = init("F88", 4601912, "N\u00B7cm", "newton centimetre");
	public static final EUInformation NEWTON_METRE = init("NU", 20053, "N\u00B7m", "newton metre");
	public static final EUInformation NEWTON_METRE_PER_AMPERE = init("F90", 4602160, "N\u00B7m/A",
			"newton metre per ampere");
	public static final EUInformation NEWTON_METRE_PER_DEGREE = init("F89", 4601913, "Nm/\u00B0",
			"newton metre per degree");
	public static final EUInformation NEWTON_METRE_PER_KILOGRAM = init("G19", 4665657, "N\u00B7m/kg",
			"newton metre per kilogram");
	public static final EUInformation NEWTON_METRE_PER_METRE = init("Q27", 5321271, "N\u00B7m/m\u00B2",
			"newton metre per metre");
	public static final EUInformation NEWTON_METRE_PER_RADIAN = init("M93", 5060915, "N\u00B7m/rad",
			"newton metre per radian");
	public static final EUInformation NEWTON_METRE_PER_SQUARE_METRE = init("M34", 5059380, "N\u00B7m/m\u00B2",
			"newton metre per square metre");
	public static final EUInformation NEWTON_METRE_SECOND = init("C53", 4404531, "N\u00B7m\u00B7s",
			"newton metre second");
	public static final EUInformation NEWTON_METRE_SQUARED_PER_KILOGRAM_SQUARED = init("C54", 4404532,
			"N\u00B7m\u00B2/kg\u00B2", "newton metre squared per kilogram squared");
	public static final EUInformation NEWTON_METRE_WATT_TO_THE_POWER_MINUS_05 = init("H41", 4731953,
			"N\u00B7m\u00B7W\u207B\u2070\u2027\u2075", "newton metre watt to the power minus 0,5");
	public static final EUInformation NEWTON_PER_AMPERE = init("H40", 4731952, "N/A", "newton per ampere");
	public static final EUInformation NEWTON_PER_CENTIMETRE = init("M23", 5059123, "N/cm", "newton per centimetre");
	public static final EUInformation NEWTON_PER_METRE = init("4P", 13392, "N/m", "newton per metre");
	public static final EUInformation NEWTON_PER_MILLIMETRE = init("F47", 4600887, "N/mm", "newton per millimetre");
	public static final EUInformation NEWTON_PER_SQUARE_CENTIMETRE = init("E01", 4534321, "N/cm\u00B2",
			"newton per square centimetre");
	public static final EUInformation NEWTON_PER_SQUARE_METRE = init("C55", 4404533, "N/m\u00B2",
			"newton per square metre");
	public static final EUInformation NEWTON_PER_SQUARE_MILLIMETRE = init("C56", 4404534, "N/mm\u00B2",
			"newton per square millimetre");
	public static final EUInformation NEWTON_SECOND = init("C57", 4404535, "N\u00B7s", "newton second");
	public static final EUInformation NEWTON_SECOND_PER_METRE = init("C58", 4404536, "N\u00B7s/m",
			"newton second per metre");
	public static final EUInformation NEWTON_SECOND_PER_SQUARE_METRE = init("N36", 5124918, "(N/m\u00B2)\u00B7s",
			"newton second per square metre");
	public static final EUInformation NEWTON_SQUARE_METRE_PER_AMPERE = init("P49", 5256249, "N\u00B7m\u00B2/A",
			"newton square metre per ampere");
	public static final EUInformation NIL = init("NIL", 5130572, "()", "nil");
	public static final EUInformation NUMBER_OF_ARTICLES = init("NAR", 5128530, "", "number of articles");
	public static final EUInformation NUMBER_OF_CELLS = init("NCL", 5129036, "", "number of cells");
	public static final EUInformation NUMBER_OF_INTERNATIONAL_UNITS = init("NIU", 5130581, "",
			"number of international units");
	public static final EUInformation NUMBER_OF_JEWELS = init("JWL", 4872012, "", "number of jewels");
	public static final EUInformation NUMBER_OF_PACKS = init("NMP", 5131600, "", "number of packs");
	public static final EUInformation NUMBER_OF_PARTS = init("NPT", 5132372, "", "number of parts");
	public static final EUInformation NUMBER_OF_WORDS = init("D68", 4470328, "", "number of words");
	public static final EUInformation OCTAVE = init("C59", 4404537, "", "octave");
	public static final EUInformation OCTET = init("Q12", 5321010, "o", "octet");
	public static final EUInformation OCTET_PER_SECOND = init("Q13", 5321011, "o/s", "octet per second");
	public static final EUInformation OHM = init("OHM", 5195853, "\u2126", "ohm");
	public static final EUInformation OHM_CENTIMETRE = init("C60", 4404784, "\u2126\u00B7cm", "ohm centimetre");
	public static final EUInformation OHM_CIRCULAR_MIL_PER_FOOT = init("P23", 5255731, "\u03A9\u00B7cmil/ft",
			"ohm circular-mil per foot");
	public static final EUInformation OHM_KILOMETRE = init("M24", 5059124, "\u2126\u00B7km", "ohm kilometre");
	public static final EUInformation OHM_METRE = init("C61", 4404785, "\u2126\u00B7m", "ohm metre");
	public static final EUInformation OHM_PER_KILOMETRE = init("F56", 4601142, "\u2126/km", "ohm per kilometre");
	public static final EUInformation OHM_PER_METRE = init("H26", 4731446, "\u2126/m", "ohm per metre");
	public static final EUInformation OHM_PER_MILE_STATUTE_MILE = init("F55", 4601141, "\u2126/mi",
			"ohm per mile (statute mile)");
	public static final EUInformation ONE = init("C62", 4404786, "1", "one");
	public static final EUInformation ONE_PER_ONE = init("Q26", 5321270, "1/1", "one per one");
	public static final EUInformation OSCILLATIONS_PER_MINUTE = init("OPM", 5197901, "o/min",
			"oscillations per minute");
	public static final EUInformation OUNCE_AVOIRDUPOIS = init("ONZ", 5197402, "oz", "ounce (avoirdupois)");
	public static final EUInformation OUNCE_AVOIRDUPOIS_FORCE = init("L40", 4994096, "ozf",
			"ounce (avoirdupois)-force");
	public static final EUInformation OUNCE_AVOIRDUPOIS_FORCE_INCH = init("L41", 4994097, "ozf\u00B7in",
			"ounce (avoirdupois)-force inch");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_CUBIC_INCH = init("L39", 4993849, "oz/in\u00B3",
			"ounce (avoirdupois) per cubic inch");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_CUBIC_YARD = init("G32", 4666162, "oz/yd\u00B3",
			"ounce (avoirdupois) per cubic yard");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_DAY = init("L33", 4993843, "oz/d",
			"ounce (avoirdupois) per day");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_GALLON_UK = init("L37", 4993847, "oz/gal (UK)",
			"ounce (avoirdupois) per gallon (UK)");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_GALLON_US = init("L38", 4993848, "oz/gal (US)",
			"ounce (avoirdupois) per gallon (US)");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_HOUR = init("L34", 4993844, "oz/h",
			"ounce (avoirdupois) per hour");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_MINUTE = init("L35", 4993845, "oz/min",
			"ounce (avoirdupois) per minute");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_SECOND = init("L36", 4993846, "oz/s",
			"ounce (avoirdupois) per second");
	public static final EUInformation OUNCE_AVOIRDUPOIS_PER_SQUARE_INCH = init("N22", 5124658, "oz/in\u00B2",
			"ounce (avoirdupois) per square inch");
	public static final EUInformation OUNCE_FOOT = init("4R", 13394, "oz\u00B7ft", "ounce foot");
	public static final EUInformation OUNCE_INCH = init("4Q", 13393, "oz\u00B7in", "ounce inch");
	public static final EUInformation OUNCE_PER_SQUARE_FOOT = init("37", 13111, "oz/ft\u00B2", "ounce per square foot");
	public static final EUInformation OUNCE_PER_SQUARE_FOOT_PER_001INCH = init("38", 13112, "oz/(ft\u00B2/cin)",
			"ounce per square foot per 0,01inch");
	public static final EUInformation OUNCE_PER_SQUARE_YARD = init("ON", 20302, "oz/yd\u00B2", "ounce per square yard");
	public static final EUInformation OUNCE_UK_FLUID_PER_DAY = init("J95", 4864309, "fl oz (UK)/d",
			"ounce (UK fluid) per day");
	public static final EUInformation OUNCE_UK_FLUID_PER_HOUR = init("J96", 4864310, "fl oz (UK)/h",
			"ounce (UK fluid) per hour");
	public static final EUInformation OUNCE_UK_FLUID_PER_MINUTE = init("J97", 4864311, "fl oz (UK)/min",
			"ounce (UK fluid) per minute");
	public static final EUInformation OUNCE_UK_FLUID_PER_SECOND = init("J98", 4864312, "fl oz (UK)/s",
			"ounce (UK fluid) per second");
	public static final EUInformation OUNCE_US_FLUID_PER_DAY = init("J99", 4864313, "fl oz (US)/d",
			"ounce (US fluid) per day");
	public static final EUInformation OUNCE_US_FLUID_PER_HOUR = init("K10", 4927792, "fl oz (US)/h",
			"ounce (US fluid) per hour");
	public static final EUInformation OUNCE_US_FLUID_PER_MINUTE = init("K11", 4927793, "fl oz (US)/min",
			"ounce (US fluid) per minute");
	public static final EUInformation OUNCE_US_FLUID_PER_SECOND = init("K12", 4927794, "fl oz (US)/s",
			"ounce (US fluid) per second");
	public static final EUInformation OUTFIT = init("11", 12593, "", "outfit");
	public static final EUInformation OVERTIME_HOUR = init("OT", 20308, "", "overtime hour");
	public static final EUInformation OZONE_DEPLETION_EQUIVALENT = init("ODE", 5194821, "",
			"ozone depletion equivalent");
	public static final EUInformation PAD = init("PD", 20548, "", "pad");
	public static final EUInformation PAGE = init("ZP", 23120, "", "page");
	public static final EUInformation PAGE_PER_INCH = init("PQ", 20561, "ppi", "page per inch");
	public static final EUInformation PAGE___FACSIMILE = init("QA", 20801, "", "page - facsimile");
	public static final EUInformation PAGE___HARDCOPY = init("QB", 20802, "", "page - hardcopy");
	public static final EUInformation PAIR = init("PR", 20562, "", "pair");
	public static final EUInformation PANEL = init("OA", 20289, "", "panel");
	public static final EUInformation PARSEC = init("C63", 4404787, "pc", "parsec");
	public static final EUInformation PART_PER_BILLION_US = init("61", 13873, "ppb", "part per billion (US)");
	public static final EUInformation PART_PER_HUNDRED_THOUSAND = init("E40", 4535344, "ppht",
			"part per hundred thousand");
	public static final EUInformation PART_PER_MILLION = init("59", 13625, "ppm", "part per million");
	public static final EUInformation PART_PER_THOUSAND = init("NX", 20056, "\u2030", "part per thousand");
	public static final EUInformation PASCAL = init("PAL", 5259596, "Pa", "pascal");
	public static final EUInformation PASCAL_CUBIC_METRE_PER_SECOND = init("G01", 4665393, "Pa\u00B7m\u00B3/s",
			"pascal cubic metre per second");
	public static final EUInformation PASCAL_LITRE_PER_SECOND = init("F99", 4602169, "Pa\u00B7l/s",
			"pascal litre per second");
	public static final EUInformation PASCAL_PER_BAR = init("F07", 4599863, "Pa/bar", "pascal per bar");
	public static final EUInformation PASCAL_PER_KELVIN = init("C64", 4404788, "Pa/K", "pascal per kelvin");
	public static final EUInformation PASCAL_PER_METRE = init("H42", 4731954, "Pa/m", "pascal per metre");
	public static final EUInformation PASCAL_SECOND = init("C65", 4404789, "Pa\u00B7s", "pascal second");
	public static final EUInformation PASCAL_SECOND_PER_BAR = init("H07", 4730935, "Pa\u00B7s/bar",
			"pascal second per bar");
	public static final EUInformation PASCAL_SECOND_PER_CUBIC_METRE = init("C66", 4404790, "Pa\u00B7s/m\u00B3",
			"pascal second per cubic metre");
	public static final EUInformation PASCAL_SECOND_PER_KELVIN = init("F77", 4601655, "Pa.s/K",
			"pascal second per kelvin");
	public static final EUInformation PASCAL_SECOND_PER_LITRE = init("M32", 5059378, "Pa\u00B7s/l",
			"pascal second per litre");
	public static final EUInformation PASCAL_SECOND_PER_METRE = init("C67", 4404791, "Pa\u00B7 s/m",
			"pascal second per metre");
	public static final EUInformation PASCAL_SQUARED_SECOND = init("P42", 5256242, "Pa\u00B2\u00B7s",
			"pascal squared second");
	public static final EUInformation PASCAL_SQUARE_METRE_PER_KILOGRAM = init("P79", 5257017, "Pa/(kg/m\u00B2)",
			"pascal square metre per kilogram");
	public static final EUInformation PASCAL_TO_THE_POWER_SUM_OF_STOICHIOMETRIC_NUMBERS = init("P98", 5257528,
			"Pa\u03A3\u03BDB", "pascal to the power sum of stoichiometric numbers");
	public static final EUInformation PEBIBIT_PER_CUBIC_METRE = init("E82", 4536370, "Pibit/m\u00B3",
			"pebibit per cubic metre");
	public static final EUInformation PEBIBIT_PER_METRE = init("E80", 4536368, "Pibit/m", "pebibit per metre");
	public static final EUInformation PEBIBIT_PER_SQUARE_METRE = init("E81", 4536369, "Pibit/m\u00B2",
			"pebibit per square metre");
	public static final EUInformation PEBIBYTE = init("E60", 4535856, "Pibyte", "pebibyte");
	public static final EUInformation PECK = init("G23", 4665907, "pk (US)", "peck");
	public static final EUInformation PECK_UK = init("L43", 4994099, "pk (UK)", "peck (UK)");
	public static final EUInformation PECK_UK_PER_DAY = init("L44", 4994100, "pk (UK)/d", "peck (UK) per day");
	public static final EUInformation PECK_UK_PER_HOUR = init("L45", 4994101, "pk (UK)/h", "peck (UK) per hour");
	public static final EUInformation PECK_UK_PER_MINUTE = init("L46", 4994102, "pk (UK)/min", "peck (UK) per minute");
	public static final EUInformation PECK_UK_PER_SECOND = init("L47", 4994103, "pk (UK)/s", "peck (UK) per second");
	public static final EUInformation PECK_US_DRY_PER_DAY = init("L48", 4994104, "pk (US dry)/d",
			"peck (US dry) per day");
	public static final EUInformation PECK_US_DRY_PER_HOUR = init("L49", 4994105, "pk (US dry)/h",
			"peck (US dry) per hour");
	public static final EUInformation PECK_US_DRY_PER_MINUTE = init("L50", 4994352, "pk (US dry)/min",
			"peck (US dry) per minute");
	public static final EUInformation PECK_US_DRY_PER_SECOND = init("L51", 4994353, "pk (US dry)/s",
			"peck (US dry) per second");
	public static final EUInformation PENNYWEIGHT = init("DWT", 4478804, "", "pennyweight");
	public static final EUInformation PEN_CALORIE = init("N1", 20017, "", "pen calorie");
	public static final EUInformation PEN_GRAM_PROTEIN = init("D23", 4469299, "", "pen gram (protein)");
	public static final EUInformation PERCENT = init("P1", 20529, "% or pct", "percent");
	public static final EUInformation PERCENT_PER_BAR = init("H96", 4733238, "%/bar", "percent per bar");
	public static final EUInformation PERCENT_PER_DECAKELVIN = init("H73", 4732723, "%/daK", "percent per decakelvin");
	public static final EUInformation PERCENT_PER_DEGREE = init("H90", 4733232, "%/\u00B0", "percent per degree");
	public static final EUInformation PERCENT_PER_DEGREE_CELSIUS = init("M25", 5059125, "%/\u00B0C",
			"percent per degree Celsius");
	public static final EUInformation PERCENT_PER_HECTOBAR = init("H72", 4732722, "%/hbar", "percent per hectobar");
	public static final EUInformation PERCENT_PER_HUNDRED = init("H93", 4733235, "%/100", "percent per hundred");
	public static final EUInformation PERCENT_PER_INCH = init("H98", 4733240, "%/in", "percent per inch");
	public static final EUInformation PERCENT_PER_KELVIN = init("H25", 4731445, "%/K", "percent per kelvin");
	public static final EUInformation PERCENT_PER_METRE = init("H99", 4733241, "%/m", "percent per metre");
	public static final EUInformation PERCENT_PER_MILLIMETRE = init("J10", 4862256, "%/mm", "percent per millimetre");
	public static final EUInformation PERCENT_PER_MONTH = init("H71", 4732721, "%/mo", "percent per month");
	public static final EUInformation PERCENT_PER_OHM = init("H89", 4732985, "%/\u2126", "percent per ohm");
	public static final EUInformation PERCENT_PER_ONE_HUNDRED_THOUSAND = init("H92", 4733234, "%/100000",
			"percent per one hundred thousand");
	public static final EUInformation PERCENT_PER_TEN_THOUSAND = init("H91", 4733233, "%/10000",
			"percent per ten thousand");
	public static final EUInformation PERCENT_PER_THOUSAND = init("H94", 4733236, "%/1000", "percent per thousand");
	public static final EUInformation PERCENT_PER_VOLT = init("H95", 4733237, "%/V", "percent per volt");
	public static final EUInformation PERCENT_VOLUME = init("VP", 22096, "", "percent volume");
	public static final EUInformation PERCENT_WEIGHT = init("60", 13872, "", "percent weight");
	public static final EUInformation PERM_0__CELSIUS_ = init("P91", 5257521, "perm (0 \u00BAC)", "perm (0 \u00BAC)");
	public static final EUInformation PERM_23__CELSIUS_ = init("P92", 5257522, "perm (23 \u00BAC)",
			"perm (23 \u00BAC)");
	public static final EUInformation PERSON = init("IE", 18757, "", "person");
	public static final EUInformation PER_MILLE_PER_PSI = init("J12", 4862258, "\u2030/psi", "per mille per psi");
	public static final EUInformation PETABIT = init("E78", 4536120, "Pbit", "petabit");
	public static final EUInformation PETABIT_PER_SECOND = init("E79", 4536121, "Pbit/s", "petabit per second");
	public static final EUInformation PETABYTE = init("E36", 4535094, "Pbyte", "petabyte");
	public static final EUInformation PETAJOULE = init("C68", 4404792, "PJ", "petajoule");
	public static final EUInformation PFERDESTAERKE = init("N12", 5124402, "PS", "Pferdestaerke");
	public static final EUInformation PFUND = init("M86", 5060662, "pfd", "pfund");
	public static final EUInformation PHON = init("C69", 4404793, "", "phon");
	public static final EUInformation PHOT = init("P26", 5255734, "ph", "phot");
	public static final EUInformation PH_POTENTIAL_OF_HYDROGEN = init("Q30", 5321520, "pH",
			"pH (potential of Hydrogen)");
	public static final EUInformation PICA = init("R1", 21041, "", "pica");
	public static final EUInformation PICOAMPERE = init("C70", 4405040, "pA", "picoampere");
	public static final EUInformation PICOCOULOMB = init("C71", 4405041, "pC", "picocoulomb");
	public static final EUInformation PICOFARAD = init("4T", 13396, "pF", "picofarad");
	public static final EUInformation PICOFARAD_PER_METRE = init("C72", 4405042, "pF/m", "picofarad per metre");
	public static final EUInformation PICOHENRY = init("C73", 4405043, "pH", "picohenry");
	public static final EUInformation PICOLITRE = init("Q33", 5321523, "pl", "picolitre");
	public static final EUInformation PICOMETRE = init("C52", 4404530, "pm", "picometre");
	public static final EUInformation PICOPASCAL_PER_KILOMETRE = init("H69", 4732473, "pPa/km",
			"picopascal per kilometre");
	public static final EUInformation PICOSECOND = init("H70", 4732720, "ps", "picosecond");
	public static final EUInformation PICOSIEMENS = init("N92", 5126450, "pS", "picosiemens");
	public static final EUInformation PICOSIEMENS_PER_METRE = init("L42", 4994098, "pS/m", "picosiemens per metre");
	public static final EUInformation PICOVOLT = init("N99", 5126457, "pV", "picovolt");
	public static final EUInformation PICOWATT = init("C75", 4405045, "pW", "picowatt");
	public static final EUInformation PICOWATT_PER_SQUARE_METRE = init("C76", 4405046, "pW/m\u00B2",
			"picowatt per square metre");
	public static final EUInformation PIECE = init("H87", 4732983, "", "piece");
	public static final EUInformation PING = init("E19", 4534585, "", "ping");
	public static final EUInformation PINT_UK = init("PTI", 5264457, "pt (UK)", "pint (UK)");
	public static final EUInformation PINT_UK_PER_DAY = init("L53", 4994355, "pt (UK)/d", "pint (UK) per day");
	public static final EUInformation PINT_UK_PER_HOUR = init("L54", 4994356, "pt (UK)/h", "pint (UK) per hour");
	public static final EUInformation PINT_UK_PER_MINUTE = init("L55", 4994357, "pt (UK)/min", "pint (UK) per minute");
	public static final EUInformation PINT_UK_PER_SECOND = init("L56", 4994358, "pt (UK)/s", "pint (UK) per second");
	public static final EUInformation PINT_US_LIQUID_PER_DAY = init("L57", 4994359, "pt (US liq.)/d",
			"pint (US liquid) per day");
	public static final EUInformation PINT_US_LIQUID_PER_HOUR = init("L58", 4994360, "pt (US liq.)/h",
			"pint (US liquid) per hour");
	public static final EUInformation PINT_US_LIQUID_PER_MINUTE = init("L59", 4994361, "pt (US liq.)/min",
			"pint (US liquid) per minute");
	public static final EUInformation PINT_US_LIQUID_PER_SECOND = init("L60", 4994608, "pt (US liq.)/s",
			"pint (US liquid) per second");
	public static final EUInformation PIPELINE_JOINT = init("JNT", 4869716, "", "pipeline joint");
	public static final EUInformation PITCH = init("PI", 20553, "", "pitch");
	public static final EUInformation PIXEL = init("E37", 4535095, "", "pixel");
	public static final EUInformation POISE = init("89", 14393, "P", "poise");
	public static final EUInformation POISE_PER_BAR = init("F06", 4599862, "P/bar", "poise per bar");
	public static final EUInformation POISE_PER_KELVIN = init("F86", 4601910, "P/K", "poise per kelvin");
	public static final EUInformation POISE_PER_PASCAL = init("N35", 5124917, "P/Pa", "poise per pascal");
	public static final EUInformation POND = init("M78", 5060408, "p", "pond");
	public static final EUInformation PORTION = init("PTN", 5264462, "PTN", "portion");
	public static final EUInformation POUND = init("LBR", 4997714, "lb", "pound");
	public static final EUInformation POUNDAL = init("M76", 5060406, "pdl", "poundal");
	public static final EUInformation POUNDAL_FOOT = init("M95", 5060917, "pdl\u00B7ft", "poundal foot");
	public static final EUInformation POUNDAL_INCH = init("M96", 5060918, "pdl\u00B7in", "poundal inch");
	public static final EUInformation POUNDAL_PER_INCH = init("N32", 5124914, "pdl/in", "poundal per inch");
	public static final EUInformation POUNDAL_PER_SQUARE_FOOT = init("N21", 5124657, "pdl/ft\u00B2",
			"poundal per square foot");
	public static final EUInformation POUNDAL_PER_SQUARE_INCH = init("N26", 5124662, "pdl/in\u00B2",
			"poundal per square inch");
	public static final EUInformation POUNDAL_SECOND_PER_SQUARE_FOOT = init("N34", 5124916, "(pdl/ft\u00B2)\u00B7s",
			"poundal second per square foot");
	public static final EUInformation POUNDAL_SECOND_PER_SQUARE_INCH = init("N42", 5125170, "(pdl/in\u00B2)\u00B7s",
			"poundal second per square inch");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_CUBIC_FOOT_DEGREE_FAHRENHEIT = init("K69", 4929081,
			"(lb/ft\u00B3)/\u00B0F", "pound (avoirdupois) per cubic foot degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_CUBIC_FOOT_PSI = init("K70", 4929328, "(lb/ft\u00B3)/psi",
			"pound (avoirdupois) per cubic foot psi");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_CUBIC_INCH_DEGREE_FAHRENHEIT = init("K75", 4929333,
			"(lb/in\u00B3)/\u00B0F", "pound (avoirdupois) per cubic inch degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_CUBIC_INCH_PSI = init("K76", 4929334, "(lb/in\u00B3)/psi",
			"pound (avoirdupois) per cubic inch psi");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_DAY = init("K66", 4929078, "lb/d",
			"pound (avoirdupois) per day");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_DEGREE_FAHRENHEIT = init("K64", 4929076, "lb/\u00B0F",
			"pound (avoirdupois) per degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_GALLON_UK = init("K71", 4929329, "lb/gal (UK)",
			"pound (avoirdupois) per gallon (UK)");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_HOUR_DEGREE_FAHRENHEIT = init("K73", 4929331,
			"(lb/h)/\u00B0F", "pound (avoirdupois) per hour degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_HOUR_PSI = init("K74", 4929332, "(lb/h)/psi",
			"pound (avoirdupois) per hour psi");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_MINUTE = init("K78", 4929336, "lb/min",
			"pound (avoirdupois) per minute");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_MINUTE_DEGREE_FAHRENHEIT = init("K79", 4929337,
			"lb/(min\u00B7\u00B0F)", "pound (avoirdupois) per minute degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_MINUTE_PSI = init("K80", 4929584, "(lb/min)/psi",
			"pound (avoirdupois) per minute psi");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_PSI = init("K77", 4929335, "lb/psi",
			"pound (avoirdupois) per psi");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_SECOND = init("K81", 4929585, "lb/s",
			"pound (avoirdupois) per second");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_SECOND_DEGREE_FAHRENHEIT = init("K82", 4929586,
			"(lb/s)/\u00B0F", "pound (avoirdupois) per second degree Fahrenheit");
	public static final EUInformation POUND_AVOIRDUPOIS_PER_SECOND_PSI = init("K83", 4929587, "(lb/s)/psi",
			"pound (avoirdupois) per second psi");
	public static final EUInformation POUND_AVOIRDUPOIS_SQUARE_FOOT = init("K65", 4929077, "lb\u00B7ft\u00B2",
			"pound (avoirdupois) square foot");
	public static final EUInformation POUND_FOOT_PER_SECOND = init("N10", 5124400, "lb\u00B7(ft/s)",
			"pound foot per second");
	public static final EUInformation POUND_FORCE = init("C78", 4405048, "lbf", "pound-force");
	public static final EUInformation POUND_FORCE_FOOT = init("M92", 5060914, "lbf\u00B7ft", "pound-force foot");
	public static final EUInformation POUND_FORCE_FOOT_PER_AMPERE = init("F22", 4600370, "lbf\u00B7ft/A",
			"pound-force foot per ampere");
	public static final EUInformation POUND_FORCE_FOOT_PER_INCH = init("P89", 5257273, "lbf\u00B7ft/in",
			"pound-force foot per inch");
	public static final EUInformation POUND_FORCE_FOOT_PER_POUND = init("G20", 4665904, "lbf\u00B7ft/lb",
			"pound-force foot per pound");
	public static final EUInformation POUND_FORCE_INCH = init("F21", 4600369, "lbf\u00B7in", "pound-force inch");
	public static final EUInformation POUND_FORCE_INCH_PER_INCH = init("P90", 5257520, "lbf\u00B7in/in",
			"pound-force inch per inch");
	public static final EUInformation POUND_FORCE_PER_FOOT = init("F17", 4600119, "lbf/ft", "pound-force per foot");
	public static final EUInformation POUND_FORCE_PER_INCH = init("F48", 4600888, "lbf/in", "pound-force per inch");
	public static final EUInformation POUND_FORCE_PER_SQUARE_FOOT = init("K85", 4929589, "lbf/ft\u00B2",
			"pound-force per square foot");
	public static final EUInformation POUND_FORCE_PER_SQUARE_INCH = init("PS", 20563, "lbf/in\u00B2",
			"pound-force per square inch");
	public static final EUInformation POUND_FORCE_PER_SQUARE_INCH_DEGREE_FAHRENHEIT = init("K86", 4929590,
			"psi/\u00B0F", "pound-force per square inch degree Fahrenheit");
	public static final EUInformation POUND_FORCE_PER_YARD = init("N33", 5124915, "lbf/yd", "pound-force per yard");
	public static final EUInformation POUND_FORCE_SECOND_PER_SQUARE_FOOT = init("K91", 4929841, "lbf\u00B7s/ft\u00B2",
			"pound-force second per square foot");
	public static final EUInformation POUND_FORCE_SECOND_PER_SQUARE_INCH = init("K92", 4929842, "lbf\u00B7s/in\u00B2",
			"pound-force second per square inch");
	public static final EUInformation POUND_INCH_PER_SECOND = init("N11", 5124401, "lb\u00B7(in/s)",
			"pound inch per second");
	public static final EUInformation POUND_INCH_SQUARED = init("F20", 4600368, "lb\u00B7in\u00B2",
			"pound inch squared");
	public static final EUInformation POUND_MOLE = init("P44", 5256244, "lbmol", "pound mole");
	public static final EUInformation POUND_MOLE_PER_MINUTE = init("P46", 5256246, "lbmol/h", "pound mole per minute");
	public static final EUInformation POUND_MOLE_PER_POUND = init("P48", 5256248, "lbmol/lb", "pound mole per pound");
	public static final EUInformation POUND_MOLE_PER_SECOND = init("P45", 5256245, "lbmol/s", "pound mole per second");
	public static final EUInformation POUND_PER_CUBIC_FOOT = init("87", 14391, "lb/ft\u00B3", "pound per cubic foot");
	public static final EUInformation POUND_PER_CUBIC_INCH = init("LA", 19521, "lb/in\u00B3", "pound per cubic inch");
	public static final EUInformation POUND_PER_CUBIC_YARD = init("K84", 4929588, "lb/yd\u00B3",
			"pound per cubic yard");
	public static final EUInformation POUND_PER_FOOT = init("P2", 20530, "lb/ft", "pound per foot");
	public static final EUInformation POUND_PER_FOOT_DAY = init("N44", 5125172, "lb/(ft\u00B7d)", "pound per foot day");
	public static final EUInformation POUND_PER_FOOT_HOUR = init("K67", 4929079, "lb/(ft\u00B7h)",
			"pound per foot hour");
	public static final EUInformation POUND_PER_FOOT_MINUTE = init("N43", 5125171, "lb/(ft\u00B7min)",
			"pound per foot minute");
	public static final EUInformation POUND_PER_FOOT_SECOND = init("K68", 4929080, "lb/(ft\u00B7s)",
			"pound per foot second");
	public static final EUInformation POUND_PER_GALLON_US = init("GE", 18245, "lb/gal (US)", "pound per gallon (US)");
	public static final EUInformation POUND_PER_HOUR = init("4U", 13397, "lb/h", "pound per hour");
	public static final EUInformation POUND_PER_INCH_OF_LENGTH = init("PO", 20559, "lb/in", "pound per inch of length");
	public static final EUInformation POUND_PER_POUND = init("M91", 5060913, "lb/lb", "pound per pound");
	public static final EUInformation POUND_PER_REAM = init("RP", 21072, "", "pound per ream");
	public static final EUInformation POUND_PER_SQUARE_FOOT = init("FP", 18000, "lb/ft\u00B2", "pound per square foot");
	public static final EUInformation POUND_PER_SQUARE_INCH_ABSOLUTE = init("80", 14384, "lb/in\u00B2",
			"pound per square inch absolute");
	public static final EUInformation POUND_PER_SQUARE_YARD = init("N25", 5124661, "lb/yd\u00B2",
			"pound per square yard");
	public static final EUInformation POUND_PER_YARD = init("M84", 5060660, "lb/yd", "pound per yard");
	public static final EUInformation PRINT_POINT = init("N3", 20019, "", "print point");
	public static final EUInformation PROOF_GALLON = init("PGL", 5261132, "", "proof gallon");
	public static final EUInformation PROOF_LITRE = init("PFL", 5260876, "", "proof litre");
	public static final EUInformation PSI_CUBIC_INCH_PER_SECOND = init("K87", 4929591, "psi\u00B7in\u00B3/s",
			"psi cubic inch per second");
	public static final EUInformation PSI_CUBIC_METRE_PER_SECOND = init("K89", 4929593, "psi\u00B7m\u00B3/s",
			"psi cubic metre per second");
	public static final EUInformation PSI_CUBIC_YARD_PER_SECOND = init("K90", 4929840, "psi\u00B7yd\u00B3/s",
			"psi cubic yard per second");
	public static final EUInformation PSI_LITRE_PER_SECOND = init("K88", 4929592, "psi\u00B7l/s",
			"psi litre per second");
	public static final EUInformation PSI_PER_INCH = init("P86", 5257270, "psi/in", "psi per inch");
	public static final EUInformation PSI_PER_PSI = init("L52", 4994354, "psi/psi", "psi per psi");
	public static final EUInformation QUAD_1015_BTUIT = init("N70", 5125936, "quad", "quad (1015 BtuIT)");
	public static final EUInformation QUARTER_OF_A_YEAR = init("QAN", 5325134, "", "quarter (of a year)");
	public static final EUInformation QUARTER_UK = init("QTR", 5330002, "Qr (UK)", "quarter (UK)");
	public static final EUInformation QUART_UK = init("QTI", 5329993, "qt (UK)", "quart (UK)");
	public static final EUInformation QUART_UK_LIQUID_PER_DAY = init("K94", 4929844, "qt (UK liq.)/d",
			"quart (UK liquid) per day");
	public static final EUInformation QUART_UK_LIQUID_PER_HOUR = init("K95", 4929845, "qt (UK liq.)/h",
			"quart (UK liquid) per hour");
	public static final EUInformation QUART_UK_LIQUID_PER_MINUTE = init("K96", 4929846, "qt (UK liq.)/min",
			"quart (UK liquid) per minute");
	public static final EUInformation QUART_UK_LIQUID_PER_SECOND = init("K97", 4929847, "qt (UK liq.)/s",
			"quart (UK liquid) per second");
	public static final EUInformation QUART_US_LIQUID_PER_DAY = init("K98", 4929848, "qt (US liq.)/d",
			"quart (US liquid) per day");
	public static final EUInformation QUART_US_LIQUID_PER_HOUR = init("K99", 4929849, "qt (US liq.)/h",
			"quart (US liquid) per hour");
	public static final EUInformation QUART_US_LIQUID_PER_MINUTE = init("L10", 4993328, "qt (US liq.)/min",
			"quart (US liquid) per minute");
	public static final EUInformation QUART_US_LIQUID_PER_SECOND = init("L11", 4993329, "qt (US liq.)/s",
			"quart (US liquid) per second");
	public static final EUInformation QUIRE = init("QR", 20818, "qr", "quire");
	public static final EUInformation RACK_UNIT = init("H80", 4732976, "U or RU", "rack unit");
	public static final EUInformation RAD = init("C80", 4405296, "rad", "rad");
	public static final EUInformation RADIAN = init("C81", 4405297, "rad", "radian");
	public static final EUInformation RADIAN_PER_METRE = init("C84", 4405300, "rad/m", "radian per metre");
	public static final EUInformation RADIAN_PER_SECOND = init("2A", 12865, "rad/s", "radian per second");
	public static final EUInformation RADIAN_PER_SECOND_SQUARED = init("2B", 12866, "rad/s\u00B2",
			"radian per second squared");
	public static final EUInformation RADIAN_SQUARE_METRE_PER_KILOGRAM = init("C83", 4405299, "rad\u00B7m\u00B2/kg",
			"radian square metre per kilogram");
	public static final EUInformation RADIAN_SQUARE_METRE_PER_MOLE = init("C82", 4405298, "rad\u00B7m\u00B2/mol",
			"radian square metre per mole");
	public static final EUInformation RATE = init("A9", 16697, "", "rate");
	public static final EUInformation RATION = init("13", 12595, "", "ration");
	public static final EUInformation REAM = init("RM", 21069, "", "ream");
	public static final EUInformation RECIPROCAL_ANGSTROM = init("C85", 4405301, "\u00C5\u207B\u00B9",
			"reciprocal angstrom");
	public static final EUInformation RECIPROCAL_BAR = init("F58", 4601144, "1/bar", "reciprocal bar");
	public static final EUInformation RECIPROCAL_CENTIMETRE = init("E90", 4536624, "cm\u207B\u00B9",
			"reciprocal centimetre");
	public static final EUInformation RECIPROCAL_CUBIC_CENTIMETRE = init("H50", 4732208, "cm\u207B\u00B3",
			"reciprocal cubic centimetre");
	public static final EUInformation RECIPROCAL_CUBIC_FOOT = init("K20", 4928048, "1/ft\u00B3",
			"reciprocal cubic foot");
	public static final EUInformation RECIPROCAL_CUBIC_INCH = init("K49", 4928569, "1/in\u00B3",
			"reciprocal cubic inch");
	public static final EUInformation RECIPROCAL_CUBIC_METRE = init("C86", 4405302, "m\u207B\u00B3",
			"reciprocal cubic metre");
	public static final EUInformation RECIPROCAL_CUBIC_METRE_PER_SECOND = init("C87", 4405303, "m\u207B\u00B3/s",
			"reciprocal cubic metre per second");
	public static final EUInformation RECIPROCAL_CUBIC_MILLIMETRE = init("L20", 4993584, "1/mm\u00B3",
			"reciprocal cubic millimetre");
	public static final EUInformation RECIPROCAL_CUBIC_YARD = init("M10", 5058864, "1/yd\u00B3",
			"reciprocal cubic yard");
	public static final EUInformation RECIPROCAL_DAY = init("E91", 4536625, "d\u207B\u00B9", "reciprocal day");
	public static final EUInformation RECIPROCAL_DEGREE_FAHRENHEIT = init("J26", 4862518, "1/\u00B0F",
			"reciprocal degree Fahrenheit");
	public static final EUInformation RECIPROCAL_ELECTRON_VOLT_PER_CUBIC_METRE = init("C88", 4405304,
			"eV\u207B\u00B9/m\u00B3", "reciprocal electron volt per cubic metre");
	public static final EUInformation RECIPROCAL_HENRY = init("C89", 4405305, "H\u207B\u00B9", "reciprocal henry");
	public static final EUInformation RECIPROCAL_HOUR = init("H10", 4731184, "1/h", "reciprocal hour");
	public static final EUInformation RECIPROCAL_INCH = init("Q24", 5321268, "1/in", "reciprocal inch");
	public static final EUInformation RECIPROCAL_JOULE = init("N91", 5126449, "1/J", "reciprocal joule");
	public static final EUInformation RECIPROCAL_JOULE_PER_CUBIC_METRE = init("C90", 4405552, "J\u207B\u00B9/m\u00B3",
			"reciprocal joule per cubic metre");
	public static final EUInformation RECIPROCAL_KELVIN_OR_KELVIN_TO_THE_POWER_MINUS_ONE = init("C91", 4405553,
			"K\u207B\u00B9", "reciprocal kelvin or kelvin to the power minus one");
	public static final EUInformation RECIPROCAL_KILOVOLT___AMPERE_RECIPROCAL_HOUR = init("M21", 5059121, "1/kVAh",
			"reciprocal kilovolt - ampere reciprocal hour");
	public static final EUInformation RECIPROCAL_LITRE = init("K63", 4929075, "1/l", "reciprocal litre");
	public static final EUInformation RECIPROCAL_MEGAKELVIN_OR_MEGAKELVIN_TO_THE_POWER_MINUS_ONE = init("M20", 5059120,
			"1/MK", "reciprocal megakelvin or megakelvin to the power minus one");
	public static final EUInformation RECIPROCAL_METRE = init("C92", 4405554, "m\u207B\u00B9", "reciprocal metre");
	public static final EUInformation RECIPROCAL_METRE_SQUARED_RECIPROCAL_SECOND = init("B81", 4339761,
			"m\u207B\u00B2/s", "reciprocal metre squared reciprocal second");
	public static final EUInformation RECIPROCAL_MINUTE = init("C94", 4405556, "min\u207B\u00B9", "reciprocal minute");
	public static final EUInformation RECIPROCAL_MOLE = init("C95", 4405557, "mol\u207B\u00B9", "reciprocal mole");
	public static final EUInformation RECIPROCAL_MONTH = init("H11", 4731185, "1/mo", "reciprocal month");
	public static final EUInformation RECIPROCAL_PASCAL_OR_PASCAL_TO_THE_POWER_MINUS_ONE = init("C96", 4405558,
			"Pa\u207B\u00B9", "reciprocal pascal or pascal to the power minus one");
	public static final EUInformation RECIPROCAL_PSI = init("K93", 4929843, "1/psi", "reciprocal psi");
	public static final EUInformation RECIPROCAL_RADIAN = init("P97", 5257527, "1/rad", "reciprocal radian");
	public static final EUInformation RECIPROCAL_SECOND = init("C97", 4405559, "s\u207B\u00B9", "reciprocal second");
	public static final EUInformation RECIPROCAL_SECOND_PER_METRE_SQUARED = init("C99", 4405561,
			"s\u207B\u00B9/m\u00B2", "reciprocal second per metre squared");
	public static final EUInformation RECIPROCAL_SECOND_PER_STERADIAN = init("D1", 17457, "s\u207B\u00B9/sr",
			"reciprocal second per steradian");
	public static final EUInformation RECIPROCAL_SECOND_PER_STERADIAN_METRE_SQUARED = init("D2", 17458,
			"s\u207B\u00B9/(sr\u00B7m\u00B2)", "reciprocal second per steradian metre squared");
	public static final EUInformation RECIPROCAL_SQUARE_INCH = init("P78", 5257016, "1/in\u00B2",
			"reciprocal square inch");
	public static final EUInformation RECIPROCAL_SQUARE_METRE = init("C93", 4405555, "m\u207B\u00B2",
			"reciprocal square metre");
	public static final EUInformation RECIPROCAL_VOLT = init("P96", 5257526, "1/V", "reciprocal volt");
	public static final EUInformation RECIPROCAL_VOLT___AMPERE_RECIPROCAL_SECOND = init("M30", 5059376,
			"1/(V\u00B7A\u00B7s)", "reciprocal volt - ampere reciprocal second");
	public static final EUInformation RECIPROCAL_WEEK = init("H85", 4732981, "1/wk", "reciprocal week");
	public static final EUInformation RECIPROCAL_YEAR = init("H09", 4730937, "1/y", "reciprocal year");
	public static final EUInformation REM = init("D91", 4471089, "rem", "rem");
	public static final EUInformation REM_PER_SECOND = init("P69", 5256761, "rem/s", "rem per second");
	public static final EUInformation REVENUE_TON_MILE = init("RT", 21076, "", "revenue ton mile");
	public static final EUInformation REVOLUTION = init("M44", 5059636, "rev", "revolution");
	public static final EUInformation REVOLUTIONS_PER_MINUTE = init("RPM", 5394509, "r/min", "revolutions per minute");
	public static final EUInformation REVOLUTIONS_PER_SECOND = init("RPS", 5394515, "r/s", "revolutions per second");
	public static final EUInformation REVOLUTION_PER_MINUTE = init("M46", 5059638, "r/min", "revolution per minute");
	public static final EUInformation RHE = init("P88", 5257272, "rhe", "rhe");
	public static final EUInformation ROD_UNIT_OF_DISTANCE = init("F49", 4600889, "rd (US)", "rod [unit of distance]");
	public static final EUInformation ROENTGEN = init("2C", 12867, "R", "roentgen");
	public static final EUInformation ROENTGEN_PER_SECOND = init("D6", 17462, "R/s", "roentgen per second");
	public static final EUInformation ROOM = init("ROM", 5394253, "", "room");
	public static final EUInformation ROUND = init("D65", 4470325, "", "round");
	public static final EUInformation RUNNING_OR_OPERATING_HOUR = init("RH", 21064, "", "running or operating hour");
	public static final EUInformation RUN_FOOT = init("E52", 4535602, "", "run foot");
	public static final EUInformation SCORE = init("SCO", 5456719, "", "score");
	public static final EUInformation SCRUPLE = init("SCR", 5456722, "", "scruple");
	public static final EUInformation SECOND_PER_CUBIC_METRE = init("D93", 4471091, "s/m\u00B3",
			"second per cubic metre");
	public static final EUInformation SECOND_PER_CUBIC_METRE_RADIAN = init("D94", 4471092, "s/(rad\u00B7m\u00B3)",
			"second per cubic metre radian");
	public static final EUInformation SECOND_PER_KILOGRAMM = init("Q20", 5321264, "s/kg", "second per kilogramm");
	public static final EUInformation SECOND_PER_RADIAN_CUBIC_METRE = init("Q22", 5321266,
			"1/(Hz\u00B7rad\u00B7m\u00B3)", "second per radian cubic metre");
	public static final EUInformation SECOND_UNIT_OF_ANGLE = init("D62", 4470322, "\"\"\"\"", "second [unit of angle]");
	public static final EUInformation SECOND_UNIT_OF_TIME = init("SEC", 5457219, "s", "second [unit of time]");
	public static final EUInformation SEGMENT = init("SG", 21319, "", "segment");
	public static final EUInformation SERVICE_UNIT = init("E48", 4535352, "", "service unit");
	public static final EUInformation SET = init("SET", 5457236, "", "set");
	public static final EUInformation SHAKE = init("M56", 5059894, "shake", "shake");
	public static final EUInformation SHANNON = init("Q14", 5321012, "Sh", "shannon");
	public static final EUInformation SHANNON_PER_SECOND = init("Q17", 5321015, "Sh/s", "shannon per second");
	public static final EUInformation SHARES = init("E21", 4534833, "", "shares");
	public static final EUInformation SHIPMENT = init("SX", 21336, "", "shipment");
	public static final EUInformation SHOT = init("14", 12596, "", "shot");
	public static final EUInformation SIDEREAL_YEAR = init("L96", 4995382, "y (sidereal)", "sidereal year");
	public static final EUInformation SIEMENS = init("SIE", 5458245, "S", "siemens");
	public static final EUInformation SIEMENS_PER_CENTIMETRE = init("H43", 4731955, "S/cm", "siemens per centimetre");
	public static final EUInformation SIEMENS_PER_METRE = init("D10", 4469040, "S/m", "siemens per metre");
	public static final EUInformation SIEMENS_SQUARE_METRE_PER_MOLE = init("D12", 4469042, "S\u00B7m\u00B2/mol",
			"siemens square metre per mole");
	public static final EUInformation SIEVERT = init("D13", 4469043, "Sv", "sievert");
	public static final EUInformation SIEVERT_PER_HOUR = init("P70", 5257008, "Sv/h", "sievert per hour");
	public static final EUInformation SIEVERT_PER_MINUTE = init("P74", 5257012, "Sv/min", "sievert per minute");
	public static final EUInformation SIEVERT_PER_SECOND = init("P65", 5256757, "Sv/s", "sievert per second");
	public static final EUInformation SITAS = init("56", 13622, "", "sitas");
	public static final EUInformation SKEIN = init("SW", 21335, "", "skein");
	public static final EUInformation SLUG = init("F13", 4600115, "slug", "slug");
	public static final EUInformation SLUG_PER_CUBIC_FOOT = init("L65", 4994613, "slug/ft\u00B3",
			"slug per cubic foot");
	public static final EUInformation SLUG_PER_DAY = init("L63", 4994611, "slug/d", "slug per day");
	public static final EUInformation SLUG_PER_FOOT_SECOND = init("L64", 4994612, "slug/(ft\u00B7s)",
			"slug per foot second");
	public static final EUInformation SLUG_PER_HOUR = init("L66", 4994614, "slug/h", "slug per hour");
	public static final EUInformation SLUG_PER_MINUTE = init("L67", 4994615, "slug/min", "slug per minute");
	public static final EUInformation SLUG_PER_SECOND = init("L68", 4994616, "slug/s", "slug per second");
	public static final EUInformation SONE = init("D15", 4469045, "", "sone");
	public static final EUInformation SQUARE = init("SQ", 21329, "", "square");
	public static final EUInformation SQUARE_CENTIMETRE = init("CMK", 4410699, "cm\u00B2", "square centimetre");
	public static final EUInformation SQUARE_CENTIMETRE_PER_ERG = init("D16", 4469046, "cm\u00B2/erg",
			"square centimetre per erg");
	public static final EUInformation SQUARE_CENTIMETRE_PER_GRAM = init("H15", 4731189, "cm\u00B2/g",
			"square centimetre per gram");
	public static final EUInformation SQUARE_CENTIMETRE_PER_SECOND = init("M81", 5060657, "cm\u00B2/s",
			"square centimetre per second");
	public static final EUInformation SQUARE_CENTIMETRE_PER_STERADIAN_ERG = init("D17", 4469047,
			"cm\u00B2/(sr\u00B7erg)", "square centimetre per steradian erg");
	public static final EUInformation SQUARE_DECAMETRE = init("H16", 4731190, "dam\u00B2", "square decametre");
	public static final EUInformation SQUARE_DECIMETRE = init("DMK", 4476235, "dm\u00B2", "square decimetre");
	public static final EUInformation SQUARE_FOOT = init("FTK", 4609099, "ft\u00B2", "square foot");
	public static final EUInformation SQUARE_FOOT_PER_HOUR = init("M79", 5060409, "ft\u00B2/h", "square foot per hour");
	public static final EUInformation SQUARE_FOOT_PER_SECOND = init("S3", 21299, "ft\u00B2/s",
			"square foot per second");
	public static final EUInformation SQUARE_HECTOMETRE = init("H18", 4731192, "hm\u00B2", "square hectometre");
	public static final EUInformation SQUARE_INCH = init("INK", 4804171, "in\u00B2", "square inch");
	public static final EUInformation SQUARE_INCH_PER_SECOND = init("G08", 4665400, "in\u00B2/s",
			"square inch per second");
	public static final EUInformation SQUARE_KILOMETRE = init("KMK", 4934987, "km\u00B2", "square kilometre");
	public static final EUInformation SQUARE_METRE = init("MTK", 5067851, "m\u00B2", "square metre");
	public static final EUInformation SQUARE_METRE_HOUR_DEGREE_CELSIUS_PER_KILOCALORIE_INTERNATIONAL_TABLE = init("L14",
			4993332, "m\u00B2\u00B7h\u00B7\u00B0C/kcal",
			"square metre hour degree Celsius per kilocalorie (international table)");
	public static final EUInformation SQUARE_METRE_KELVIN_PER_WATT = init("D19", 4469049, "m\u00B2\u00B7K/W",
			"square metre kelvin per watt");
	public static final EUInformation SQUARE_METRE_PER_CUBIC_METRE = init("Q36", 5321526, "m2/m3",
			"square metre per cubic metre");
	public static final EUInformation SQUARE_METRE_PER_JOULE = init("D20", 4469296, "m\u00B2/J",
			"square metre per joule");
	public static final EUInformation SQUARE_METRE_PER_KILOGRAM = init("D21", 4469297, "m\u00B2/kg",
			"square metre per kilogram");
	public static final EUInformation SQUARE_METRE_PER_LITRE = init("E31", 4535089, "m\u00B2/l",
			"square metre per litre");
	public static final EUInformation SQUARE_METRE_PER_MOLE = init("D22", 4469298, "m\u00B2/mol",
			"square metre per mole");
	public static final EUInformation SQUARE_METRE_PER_NEWTON = init("H59", 4732217, "m\u00B2/N",
			"square metre per newton");
	public static final EUInformation SQUARE_METRE_PER_SECOND = init("S4", 21300, "m\u00B2/s",
			"square metre per second");
	public static final EUInformation SQUARE_METRE_PER_SECOND_BAR = init("G41", 4666417, "m\u00B2/(s\u00B7bar)",
			"square metre per second bar");
	public static final EUInformation SQUARE_METRE_PER_SECOND_KELVIN = init("G09", 4665401, "m\u00B2/(s\u00B7K)",
			"square metre per second kelvin");
	public static final EUInformation SQUARE_METRE_PER_SECOND_PASCAL = init("M82", 5060658, "(m\u00B2/s)/Pa",
			"square metre per second pascal");
	public static final EUInformation SQUARE_METRE_PER_STERADIAN = init("D24", 4469300, "m\u00B2/sr",
			"square metre per steradian");
	public static final EUInformation SQUARE_METRE_PER_STERADIAN_JOULE = init("D25", 4469301, "m\u00B2/(sr\u00B7J)",
			"square metre per steradian joule");
	public static final EUInformation SQUARE_METRE_PER_VOLT_SECOND = init("D26", 4469302, "m\u00B2/(V\u00B7s)",
			"square metre per volt second");
	public static final EUInformation SQUARE_MICROMETRE_SQUARE_MICRON = init("H30", 4731696, "\u00B5m\u00B2",
			"square micrometre (square micron)");
	public static final EUInformation SQUARE_MILE_BASED_ON_US_SURVEY_FOOT = init("M48", 5059640, "mi\u00B2 (US survey)",
			"square mile (based on U.S. survey foot)");
	public static final EUInformation SQUARE_MILE_STATUTE_MILE = init("MIK", 5065035, "mi\u00B2",
			"square mile (statute mile)");
	public static final EUInformation SQUARE_MILLIMETRE = init("MMK", 5066059, "mm\u00B2", "square millimetre");
	public static final EUInformation SQUARE_ROOFING = init("SQR", 5460306, "", "square, roofing");
	public static final EUInformation SQUARE_YARD = init("YDK", 5850187, "yd\u00B2", "square yard");
	public static final EUInformation STANDARD = init("WSD", 5722948, "std", "standard");
	public static final EUInformation STANDARD_ACCELERATION_OF_FREE_FALL = init("K40", 4928560, "gn",
			"standard acceleration of free fall");
	public static final EUInformation STANDARD_ATMOSPHERE = init("ATM", 4281421, "atm", "standard atmosphere");
	public static final EUInformation STANDARD_ATMOSPHERE_PER_METRE = init("P83", 5257267, "Atm/m",
			"standard atmosphere per metre");
	public static final EUInformation STANDARD_KILOLITRE = init("DMO", 4476239, "", "standard kilolitre");
	public static final EUInformation STANDARD_LITRE = init("STL", 5461068, "", "standard litre");
	public static final EUInformation STERADIAN = init("D27", 4469303, "sr", "steradian");
	public static final EUInformation STERE = init("G26", 4665910, "st", "stere");
	public static final EUInformation STICK = init("STC", 5461059, "", "stick");
	public static final EUInformation STICK_CIGARETTE = init("STK", 5461067, "", "stick, cigarette");
	public static final EUInformation STICK_MILITARY = init("15", 12597, "", "stick, military");
	public static final EUInformation STILB = init("P31", 5255985, "sb", "stilb");
	public static final EUInformation STOKES = init("91", 14641, "St", "stokes");
	public static final EUInformation STOKES_PER_BAR = init("G46", 4666422, "St/bar", "stokes per bar");
	public static final EUInformation STOKES_PER_KELVIN = init("G10", 4665648, "St/K", "stokes per kelvin");
	public static final EUInformation STOKES_PER_PASCAL = init("M80", 5060656, "St/Pa", "stokes per pascal");
	public static final EUInformation STONE_UK = init("STI", 5461065, "st", "stone (UK)");
	public static final EUInformation STRAND = init("E30", 4535088, "", "strand");
	public static final EUInformation STRAW = init("STW", 5461079, "", "straw");
	public static final EUInformation STRIP = init("SR", 21330, "", "strip");
	public static final EUInformation SYRINGE = init("SYR", 5462354, "", "syringe");
	public static final EUInformation TABLESPOON_US = init("G24", 4665908, "tablespoon (US)", "tablespoon (US)");
	public static final EUInformation TABLET = init("U2", 21810, "", "tablet");
	public static final EUInformation TEASPOON_US = init("G25", 4665909, "teaspoon (US)", "teaspoon (US)");
	public static final EUInformation TEBIBIT_PER_CUBIC_METRE = init("E86", 4536374, "Tibit/m\u00B3",
			"tebibit per cubic metre");
	public static final EUInformation TEBIBIT_PER_METRE = init("E85", 4536373, "Tibit/m", "tebibit per metre");
	public static final EUInformation TEBIBIT_PER_SQUARE_METRE = init("E87", 4536375, "Tibit/m\u00B2",
			"tebibit per square metre");
	public static final EUInformation TEBIBYTE = init("E61", 4535857, "Tibyte", "tebibyte");
	public static final EUInformation TECHNICAL_ATMOSPHERE_PER_METRE = init("P84", 5257268, "at/m",
			"technical atmosphere per metre");
	public static final EUInformation TEETH_PER_INCH = init("TPI", 5525577, "TPI", "teeth per inch");
	public static final EUInformation TELECOMMUNICATION_LINE_IN_SERVICE = init("T0", 21552, "",
			"telecommunication line in service");
	public static final EUInformation TELECOMMUNICATION_LINE_IN_SERVICE_AVERAGE = init("UB", 21826, "",
			"telecommunication line in service average");
	public static final EUInformation TELECOMMUNICATION_PORT = init("UC", 21827, "", "telecommunication port");
	public static final EUInformation TEN_DAY = init("DAD", 4473156, "", "ten day");
	public static final EUInformation TEN_PACK = init("TP", 21584, "", "ten pack");
	public static final EUInformation TEN_PAIR = init("TPR", 5525586, "", "ten pair");
	public static final EUInformation TEN_SET = init("TST", 5526356, "", "ten set");
	public static final EUInformation TEN_THOUSAND_STICKS = init("TTS", 5526611, "", "ten thousand sticks");
	public static final EUInformation TERABIT = init("E83", 4536371, "Tbit", "terabit");
	public static final EUInformation TERABIT_PER_SECOND = init("E84", 4536372, "Tbit/s", "terabit per second");
	public static final EUInformation TERABYTE = init("E35", 4535093, "Tbyte", "terabyte");
	public static final EUInformation TERAHERTZ = init("D29", 4469305, "THz", "terahertz");
	public static final EUInformation TERAJOULE = init("D30", 4469552, "TJ", "terajoule");
	public static final EUInformation TERAOHM = init("H44", 4731956, "T\u2126", "teraohm");
	public static final EUInformation TERAWATT = init("D31", 4469553, "TW", "terawatt");
	public static final EUInformation TERAWATT_HOUR = init("D32", 4469554, "TW\u00B7h", "terawatt hour");
	public static final EUInformation TESLA = init("D33", 4469555, "T", "tesla");
	public static final EUInformation TEST = init("E53", 4535603, "", "test");
	public static final EUInformation TEU = init("E22", 4534834, "", "TEU");
	public static final EUInformation TEX = init("D34", 4469556, "tex (g/km)", "tex");
	public static final EUInformation THEORETICAL_POUND = init("24", 12852, "", "theoretical pound");
	public static final EUInformation THEORETICAL_TON = init("27", 12855, "", "theoretical ton");
	public static final EUInformation THERM_EC = init("N71", 5125937, "thm (EC)", "therm (EC)");
	public static final EUInformation THERM_US = init("N72", 5125938, "thm (US)", "therm (U.S.)");
	public static final EUInformation THOUSAND = init("MIL", 5065036, "", "thousand");
	public static final EUInformation THOUSAND_BOARD_FOOT = init("MBF", 5063238, "", "thousand board foot");
	public static final EUInformation THOUSAND_CUBIC_FOOT = init("FC", 17987, "kft\u00B3", "thousand cubic foot");
	public static final EUInformation THOUSAND_CUBIC_METRE = init("R9", 21049, "", "thousand cubic metre");
	public static final EUInformation THOUSAND_CUBIC_METRE_PER_DAY = init("TQD", 5525828, "km\u00B3/d",
			"thousand cubic metre per day");
	public static final EUInformation THOUSAND_PIECE = init("T3", 21555, "", "thousand piece");
	public static final EUInformation THOUSAND_SQUARE_INCH = init("TI", 21577, "", "thousand square inch");
	public static final EUInformation THOUSAND_STANDARD_BRICK_EQUIVALENT = init("MBE", 5063237, "",
			"thousand standard brick equivalent");
	public static final EUInformation TONNE_KILOMETRE = init("TKM", 5524301, "t\u00B7km", "tonne kilometre");
	public static final EUInformation TONNE_METRIC_TON = init("TNE", 5525061, "t", "tonne (metric ton)");
	public static final EUInformation TONNE_PER_BAR = init("L70", 4994864, "t/bar", "tonne per bar");
	public static final EUInformation TONNE_PER_CUBIC_METRE = init("D41", 4469809, "t/m\u00B3",
			"tonne per cubic metre");
	public static final EUInformation TONNE_PER_CUBIC_METRE_BAR = init("L77", 4994871, "(t/m\u00B3)/bar",
			"tonne per cubic metre bar");
	public static final EUInformation TONNE_PER_CUBIC_METRE_KELVIN = init("L76", 4994870, "(t/m\u00B3)/K",
			"tonne per cubic metre kelvin");
	public static final EUInformation TONNE_PER_DAY = init("L71", 4994865, "t/d", "tonne per day");
	public static final EUInformation TONNE_PER_DAY_BAR = init("L73", 4994867, "(t/d)/bar", "tonne per day bar");
	public static final EUInformation TONNE_PER_DAY_KELVIN = init("L72", 4994866, "(t/d)/K", "tonne per day kelvin");
	public static final EUInformation TONNE_PER_HOUR = init("E18", 4534584, "t/h", "tonne per hour");
	public static final EUInformation TONNE_PER_HOUR_BAR = init("L75", 4994869, "(t/h)/bar", "tonne per hour bar");
	public static final EUInformation TONNE_PER_HOUR_KELVIN = init("L74", 4994868, "(t/h)/K", "tonne per hour kelvin");
	public static final EUInformation TONNE_PER_KELVIN = init("L69", 4994617, "t/K", "tonne per kelvin");
	public static final EUInformation TONNE_PER_MINUTE = init("L78", 4994872, "t/min", "tonne per minute");
	public static final EUInformation TONNE_PER_MINUTE_BAR = init("L80", 4995120, "(t/min)/bar",
			"tonne per minute bar");
	public static final EUInformation TONNE_PER_MINUTE_KELVIN = init("L79", 4994873, "(t/min)/K",
			"tonne per minute kelvin");
	public static final EUInformation TONNE_PER_MONTH = init("M88", 5060664, "t/mo", "tonne per month");
	public static final EUInformation TONNE_PER_SECOND = init("L81", 4995121, "t/s", "tonne per second");
	public static final EUInformation TONNE_PER_SECOND_BAR = init("L83", 4995123, "(t/s)/bar", "tonne per second bar");
	public static final EUInformation TONNE_PER_SECOND_KELVIN = init("L82", 4995122, "(t/s)/K",
			"tonne per second kelvin");
	public static final EUInformation TONNE_PER_YEAR = init("M89", 5060665, "t/y", "tonne per year");
	public static final EUInformation TON_ASSAY = init("M85", 5060661, "", "ton, assay");
	public static final EUInformation TON_FORCE_US_SHORT = init("L94", 4995380, "ton.sh-force", "ton-force (US short)");
	public static final EUInformation TON_LONG_PER_DAY = init("L85", 4995125, "ton (UK)/d", "ton long per day");
	public static final EUInformation TON_REGISTER = init("M70", 5060400, "RT", "ton, register");
	public static final EUInformation TON_SHORT_PER_DAY = init("L88", 4995128, "ton (US)/d", "ton short per day");
	public static final EUInformation TON_SHORT_PER_DEGREE_FAHRENHEIT = init("L87", 4995127, "ton (US)/\u00B0F",
			"ton short per degree Fahrenheit");
	public static final EUInformation TON_SHORT_PER_HOUR_DEGREE_FAHRENHEIT = init("L89", 4995129,
			"ton (US)/(h\u00B7\u00B0F)", "ton short per hour degree Fahrenheit");
	public static final EUInformation TON_SHORT_PER_HOUR_PSI = init("L90", 4995376, "(ton (US)/h)/psi",
			"ton short per hour psi");
	public static final EUInformation TON_SHORT_PER_PSI = init("L91", 4995377, "ton (US)/psi", "ton short per psi");
	public static final EUInformation TON_UK_LONG_PER_CUBIC_YARD = init("L92", 4995378, "ton.l/yd\u00B3 (UK)",
			"ton (UK long) per cubic yard");
	public static final EUInformation TON_UK_OR_LONG_TON_US = init("LTN", 5002318, "ton (UK)",
			"ton (UK) or long ton (US)");
	public static final EUInformation TON_UK_SHIPPING = init("L84", 4995124, "British shipping ton",
			"ton (UK shipping)");
	public static final EUInformation TON_US_OR_SHORT_TON_UK_PER_US = init("STN", 5461070, "ton (US)",
			"ton (US) or short ton (UK/US)");
	public static final EUInformation TON_US_PER_HOUR = init("4W", 13399, "ton (US) /h", "ton (US) per hour");
	public static final EUInformation TON_US_SHIPPING = init("L86", 4995126, "(US) shipping ton", "ton (US shipping)");
	public static final EUInformation TON_US_SHORT_PER_CUBIC_YARD = init("L93", 4995379, "ton.s/yd\u00B3 (US)",
			"ton (US short) per cubic yard");
	public static final EUInformation TORR_PER_METRE = init("P85", 5257269, "Torr/m", "torr per metre");
	public static final EUInformation TOTAL_ACID_NUMBER = init("TAN", 5521742, "TAN", "total acid number");
	public static final EUInformation TREATMENT = init("U1", 21809, "", "treatment");
	public static final EUInformation TRILLION_EUR = init("TRL", 5526092, "", "trillion (EUR)");
	public static final EUInformation TRIP = init("E54", 4535604, "", "trip");
	public static final EUInformation TROPICAL_YEAR = init("D42", 4469810, "y (tropical)", "tropical year");
	public static final EUInformation TROY_OUNCE_OR_APOTHECARY_OUNCE = init("APZ", 4280410, "tr oz",
			"troy ounce or apothecary ounce");
	public static final EUInformation TROY_POUND_US = init("LBT", 4997716, "", "troy pound (US)");
	public static final EUInformation TWENTY_FOOT_CONTAINER = init("20", 12848, "", "twenty foot container");
	public static final EUInformation TYRE = init("E23", 4534835, "", "tyre");
	public static final EUInformation UNIFIED_ATOMIC_MASS_UNIT = init("D43", 4469811, "u", "unified atomic mass unit");
	public static final EUInformation UNIT_POLE = init("P53", 5256499, "unit pole", "unit pole");
	public static final EUInformation USE = init("E55", 4535605, "", "use");
	public static final EUInformation US_GALLON_PER_MINUTE = init("G2", 18226, "gal (US) /min", "US gallon per minute");
	public static final EUInformation VAR = init("D44", 4469812, "var", "var");
	public static final EUInformation VOLT = init("VLT", 5655636, "V", "volt");
	public static final EUInformation VOLT_AC = init("2G", 12871, "V", "volt AC");
	public static final EUInformation VOLT_DC = init("2H", 12872, "V", "volt DC");
	public static final EUInformation VOLT_PER_BAR = init("G60", 4666928, "V/bar", "volt per bar");
	public static final EUInformation VOLT_PER_CENTIMETRE = init("D47", 4469815, "V/cm", "volt per centimetre");
	public static final EUInformation VOLT_PER_INCH = init("H23", 4731443, "V/in", "volt per inch");
	public static final EUInformation VOLT_PER_KELVIN = init("D48", 4469816, "V/K", "volt per kelvin");
	public static final EUInformation VOLT_PER_LITRE_MINUTE = init("F87", 4601911, "V/(l\u00B7min)",
			"volt per litre minute");
	public static final EUInformation VOLT_PER_METRE = init("D50", 4470064, "V/m", "volt per metre");
	public static final EUInformation VOLT_PER_MICROSECOND = init("H24", 4731444, "V/\u00B5s", "volt per microsecond");
	public static final EUInformation VOLT_PER_MILLIMETRE = init("D51", 4470065, "V/mm", "volt per millimetre");
	public static final EUInformation VOLT_PER_PASCAL = init("N98", 5126456, "V/Pa", "volt per pascal");
	public static final EUInformation VOLT_PER_SECOND = init("H46", 4731958, "V/s", "volt per second");
	public static final EUInformation VOLT_SECOND_PER_METRE = init("H45", 4731957, "V\u00B7s/m",
			"volt second per metre");
	public static final EUInformation VOLT_SQUARED_PER_KELVIN_SQUARED = init("D45", 4469813, "V\u00B2/K\u00B2",
			"volt squared per kelvin squared");
	public static final EUInformation VOLT_SQUARE_INCH_PER_POUND_FORCE = init("H22", 4731442, "V/(lbf/in\u00B2)",
			"volt square inch per pound-force");
	public static final EUInformation VOLT___AMPERE = init("D46", 4469814, "V\u00B7A", "volt - ampere");
	public static final EUInformation VOLT___AMPERE_PER_KILOGRAM = init("VA", 22081, "V\u00B7A / kg",
			"volt - ampere per kilogram");
	public static final EUInformation WATER_HORSE_POWER = init("F80", 4601904, "", "water horse power");
	public static final EUInformation WATT = init("WTT", 5723220, "W", "watt");
	public static final EUInformation WATT_HOUR = init("WHR", 5720146, "W\u00B7h", "watt hour");
	public static final EUInformation WATT_PER_CUBIC_METRE = init("H47", 4731959, "W/m\u00B3", "watt per cubic metre");
	public static final EUInformation WATT_PER_KELVIN = init("D52", 4470066, "W/K", "watt per kelvin");
	public static final EUInformation WATT_PER_KILOGRAM = init("WA", 22337, "W/kg", "watt per kilogram");
	public static final EUInformation WATT_PER_METRE = init("H74", 4732724, "W/m", "watt per metre");
	public static final EUInformation WATT_PER_METRE_DEGREE_CELSIUS = init("N80", 5126192, "W/(m\u00B7\u00B0C)",
			"watt per metre degree Celsius");
	public static final EUInformation WATT_PER_METRE_KELVIN = init("D53", 4470067, "W/(m\u00B7K)",
			"watt per metre kelvin");
	public static final EUInformation WATT_PER_SQUARE_CENTIMETRE = init("N48", 5125176, "W/cm\u00B2",
			"watt per square centimetre");
	public static final EUInformation WATT_PER_SQUARE_INCH = init("N49", 5125177, "W/in\u00B2", "watt per square inch");
	public static final EUInformation WATT_PER_SQUARE_METRE = init("D54", 4470068, "W/m\u00B2",
			"watt per square metre");
	public static final EUInformation WATT_PER_SQUARE_METRE_KELVIN = init("D55", 4470069, "W/(m\u00B2\u00B7K)",
			"watt per square metre kelvin");
	public static final EUInformation WATT_PER_SQUARE_METRE_KELVIN_TO_THE_FOURTH_POWER = init("D56", 4470070,
			"W/(m\u00B2\u00B7K\u2074)", "watt per square metre kelvin to the fourth power");
	public static final EUInformation WATT_PER_STERADIAN = init("D57", 4470071, "W/sr", "watt per steradian");
	public static final EUInformation WATT_PER_STERADIAN_SQUARE_METRE = init("D58", 4470072, "W/(sr\u00B7m\u00B2)",
			"watt per steradian square metre");
	public static final EUInformation WATT_SECOND = init("J55", 4863285, "W\u00B7s", "watt second");
	public static final EUInformation WATT_SQUARE_METRE = init("Q21", 5321265, "W\u00B7m\u00B2", "watt square metre");
	public static final EUInformation WEBER = init("WEB", 5719362, "Wb", "weber");
	public static final EUInformation WEBER_METRE = init("P50", 5256496, "Wb\u00B7m", "weber metre");
	public static final EUInformation WEBER_PER_METRE = init("D59", 4470073, "Wb/m", "weber per metre");
	public static final EUInformation WEBER_PER_MILLIMETRE = init("D60", 4470320, "Wb/mm", "weber per millimetre");
	public static final EUInformation WEBER_TO_THE_POWER_MINUS_ONE = init("Q23", 5321267, "1/Wb",
			"weber to the power minus one");
	public static final EUInformation WEEK = init("WEE", 5719365, "wk", "week");
	public static final EUInformation WELL = init("E56", 4535606, "", "well");
	public static final EUInformation WET_KILO = init("W2", 22322, "", "wet kilo");
	public static final EUInformation WET_POUND = init("WB", 22338, "", "wet pound");
	public static final EUInformation WET_TON = init("WE", 22341, "", "wet ton");
	public static final EUInformation WINE_GALLON = init("WG", 22343, "", "wine gallon");
	public static final EUInformation WORKING_DAY = init("E49", 4535353, "", "working day");
	public static final EUInformation WORKING_MONTH = init("WM", 22349, "", "working month");
	public static final EUInformation YARD = init("YRD", 5853764, "yd", "yard");
	public static final EUInformation YARD_PER_DEGREE_FAHRENHEIT = init("L98", 4995384, "yd/\u00B0F",
			"yard per degree Fahrenheit");
	public static final EUInformation YARD_PER_HOUR = init("M66", 5060150, "yd/h", "yard per hour");
	public static final EUInformation YARD_PER_MINUTE = init("M65", 5060149, "yd/min", "yard per minute");
	public static final EUInformation YARD_PER_PSI = init("L99", 4995385, "yd/psi", "yard per psi");
	public static final EUInformation YARD_PER_SECOND = init("M64", 5060148, "yd/s", "yard per second");
	public static final EUInformation YARD_PER_SECOND_SQUARED = init("M40", 5059632, "yd/s\u00B2",
			"yard per second squared");
	public static final EUInformation YEAR = init("ANN", 4279886, "y", "year");
	public static final EUInformation ZONE = init("E57", 4535607, "", "zone");
	public static final EUInformation _30_DAY_MONTH = init("M36", 5059382, "mo (30 days)", "30-day month");
	public static final EUInformation _8_PART_CLOUD_COVER = init("A59", 4273465, "", "8-part cloud cover");

	public static EUInformation getByUnitId(int unitId) {
		return unitMap.get(unitId);
	}

	public static EUInformation getByCommonCode(String commonCode) {
		return ccUnitMap.get(commonCode);
	}

	public static List<EUInformation> getAll() {
		List<EUInformation> r = new ArrayList<EUInformation>();
		for (EUInformation eui : all) {
			r.add(eui.clone());
		}
		return r;
	}

	private static EUInformation init(String commonCode, int unitId, String displayName, String description) {
		EUInformation unit = new EUInformation(URI, unitId, new LocalizedText(displayName),
				new LocalizedText(description));
		unitMap.put(unitId, unit);
		ccUnitMap.put(commonCode, unit);
		all.add(unit);
		return unit;
	}

}
