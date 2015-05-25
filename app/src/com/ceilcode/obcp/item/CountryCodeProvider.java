package com.ceilcode.obcp.item;

import java.util.ArrayList;

public class CountryCodeProvider {

	public static class Country {
		private String code;
		private String name;

		public Country(String name, String code) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name + " " + code;
		}

	}

	public static final Country[] ALL_COUNTRY = {
			new Country("Afghanistan", "93"), new Country("Albania", "355"),
			new Country("Algeria", "213"), new Country("American Samoa", "1"),
			new Country("Andorra", "376"), new Country("Angola", "244"),
			new Country("Anguilla", "1"),
			new Country("Antigua and Barbuda", "1"),
			new Country("Argentina", "54"), new Country("Armenia", "374"),
			new Country("Aruba", "297"), new Country("Ascension", "247"),
			new Country("Australia", "61"), new Country("Austria", "43"),
			new Country("Azerbaijan", "994"), new Country("Bahamas", "1"),
			new Country("Bahrain", "973"), new Country("Bangladesh", "880"),
			new Country("Barbados", "1"), new Country("Belarus", "375"),
			new Country("Belgium", "32"), new Country("Belize", "501"),
			new Country("Benin", "229"), new Country("Bermuda", "1"),
			new Country("Bhutan", "975"), new Country("Bolivia", "591"),
			new Country("Bosnia and Herzegovina", "387"),
			new Country("Botswana", "267"), new Country("Brazil", "55"),
			new Country("British Virgin Islands", "1"),
			new Country("Brunei", "673"), new Country("Bulgaria", "359"),
			new Country("Burkina Faso", "226"), new Country("Burundi", "257"),
			new Country("Cambodia", "855"), new Country("Cameroon", "237"),
			new Country("Canada", "1"), new Country("Cape Verde", "238"),
			new Country("Cayman Islands", "1"),
			new Country("Central African Republic", "236"),
			new Country("Chad", "235"), new Country("Chile", "56"),
			new Country("China", "86"), new Country("Colombia", "57"),
			new Country("Comoros", "269"), new Country("Congo", "242"),
			new Country("Cook Islands", "682"),
			new Country("Costa Rica", "506"), new Country("Croatia", "385"),
			new Country("Cuba", "53"), new Country("Curacao", "599"),
			new Country("Cyprus", "357"), new Country("Czech Republic", "420"),
			new Country("Democratic Republic of Congo", "243"),
			new Country("Denmark", "45"), new Country("Diego Garcia", "246"),
			new Country("Djibouti", "253"), new Country("Dominica", "1"),
			new Country("Dominican Republic", "1"),
			new Country("East Timor", "670"), new Country("Ecuador", "593"),
			new Country("Egypt", "20"), new Country("El Salvador", "503"),
			new Country("Equatorial Guinea", "240"),
			new Country("Eritrea", "291"), new Country("Estonia", "372"),
			new Country("Ethiopia", "251"),
			new Country("Falkland (Malvinas) Islands", "500"),
			new Country("Faroe Islands", "298"), new Country("Fiji", "679"),
			new Country("Finland", "358"), new Country("France", "33"),
			new Country("French Guiana", "594"),
			new Country("French Polynesia", "689"),
			new Country("Gabon", "241"), new Country("Gambia", "220"),
			new Country("Georgia", "995"), new Country("Germany", "49"),
			new Country("Ghana", "233"), new Country("Gibraltar", "350"),
			new Country("Greece", "30"), new Country("Greenland", "299"),
			new Country("Grenada", "1"), new Country("Guadeloupe", "590"),
			new Country("Guam", "1"), new Country("Guatemala", "502"),
			new Country("Guinea", "224"), new Country("Guinea-Bissau", "245"),
			new Country("Guyana", "592"), new Country("Haiti", "509"),
			new Country("Honduras", "504"), new Country("Hong Kong", "852"),
			new Country("Hungary", "36"), new Country("Iceland", "354"),
			new Country("India", "91"), new Country("Indonesia", "62"),
			new Country("Inmarsat Satellite", "870"),
			new Country("Iran", "98"), new Country("Iraq", "964"),
			new Country("Ireland", "353"),
			new Country("Iridium Satellite", "8816"),
			new Country("Iridium Satellite", "8817"),
			new Country("Israel", "972"), new Country("Italy", "39"),
			new Country("Ivory Coast", "225"), new Country("Jamaica", "1"),
			new Country("Japan", "81"), new Country("Jordan", "962"),
			new Country("Kazakhstan", "7"), new Country("Kenya", "254"),
			new Country("Kiribati", "686"), new Country("Kuwait", "965"),
			new Country("Kyrgyzstan", "996"), new Country("Laos", "856"),
			new Country("Latvia", "371"), new Country("Lebanon", "961"),
			new Country("Lesotho", "266"), new Country("Liberia", "231"),
			new Country("Libya", "218"), new Country("Liechtenstein", "423"),
			new Country("Lithuania", "370"), new Country("Luxembourg", "352"),
			new Country("Macau", "853"), new Country("Macedonia", "389"),
			new Country("Madagascar", "261"), new Country("Malawi", "265"),
			new Country("Malaysia", "60"), new Country("Maldives", "960"),
			new Country("Mali", "223"), new Country("Malta", "356"),
			new Country("Marshall Islands", "692"),
			new Country("Martinique", "596"), new Country("Mauritania", "222"),
			new Country("Mauritius", "230"), new Country("Mayotte", "262"),
			new Country("Mexico", "52"), new Country("Micronesia", "691"),
			new Country("Moldova", "373"), new Country("Monaco", "377"),
			new Country("Mongolia", "976"), new Country("Montenegro", "382"),
			new Country("Montserrat", "1"), new Country("Morocco", "212"),
			new Country("Mozambique", "258"), new Country("Myanmar", "95"),
			new Country("Namibia", "264"), new Country("Nauru", "674"),
			new Country("Nepal", "977"), new Country("Netherlands", "31"),
			new Country("Netherlands Antilles", "599"),
			new Country("New Caledonia", "687"),
			new Country("New Zealand", "64"), new Country("Nicaragua", "505"),
			new Country("Niger", "227"), new Country("Nigeria", "234"),
			new Country("Niue", "683"), new Country("Norfolk Island", "6723"),
			new Country("North Korea", "850"),
			new Country("Northern Marianas", "1"), new Country("Norway", "47"),
			new Country("Oman", "968"), new Country("Pakistan", "92"),
			new Country("Palau", "680"), new Country("Palestine", "970"),
			new Country("Panama", "507"),
			new Country("Papua New Guinea", "675"),
			new Country("Paraguay", "595"), new Country("Peru", "51"),
			new Country("Philippines", "63"), new Country("Poland", "48"),
			new Country("Portugal", "351"), new Country("Puerto Rico", "1"),
			new Country("Qatar", "974"), new Country("Reunion", "262"),
			new Country("Romania", "40"),
			new Country("Russian Federation", "7"),
			new Country("Rwanda", "250"), new Country("Saint Helena", "290"),
			new Country("Saint Kitts and Nevis", "1"),
			new Country("Saint Lucia", "1"),
			new Country("Saint Barthelemy", "590"),
			new Country("Saint Martin (French part)", "590"),
			new Country("Saint Pierre and Miquelon", "508"),
			new Country("Saint Vincent and the Grenadines", "1"),
			new Country("Samoa", "685"), new Country("San Marino", "378"),
			new Country("Sao Tome and Principe", "239"),
			new Country("Saudi Arabia", "966"), new Country("Senegal", "221"),
			new Country("Serbia", "381"), new Country("Seychelles", "248"),
			new Country("Sierra Leone", "232"), new Country("Singapore", "65"),
			new Country("Sint Maarten", "1"), new Country("Slovakia", "421"),
			new Country("Slovenia", "386"),
			new Country("Solomon Islands", "677"),
			new Country("Somalia", "252"), new Country("South Africa", "27"),
			new Country("South Korea", "82"),
			new Country("South Sudan", "211"), new Country("Spain", "34"),
			new Country("Sri Lanka", "94"), new Country("Sudan", "249"),
			new Country("Suriname", "597"), new Country("Swaziland", "268"),
			new Country("Sweden", "46"), new Country("Switzerland", "41"),
			new Country("Syria", "963"), new Country("Taiwan", "886"),
			new Country("Tajikistan", "992"), new Country("Tanzania", "255"),
			new Country("Thailand", "66"),
			new Country("Thuraya Satellite", "88216"),
			new Country("Togo", "228"), new Country("Tokelau", "690"),
			new Country("Tonga", "676"),
			new Country("Trinidad and Tobago", "1"),
			new Country("Tunisia", "216"), new Country("Turkey", "90"),
			new Country("Turkmenistan", "993"),
			new Country("Turks and Caicos Islands", "1"),
			new Country("Tuvalu", "688"), new Country("Uganda", "256"),
			new Country("Ukraine", "380"),
			new Country("United Arab Emirates", "971"),
			new Country("United Kingdom", "44"),
			new Country("United States of America", "1"),
			new Country("U.S. Virgin Islands", "1"),
			new Country("Uruguay", "598"), new Country("Uzbekistan", "998"),
			new Country("Vanuatu", "678"), new Country("Vatican City", "379"),
			new Country("Vatican City", "39"), new Country("Venezuela", "58"),
			new Country("Vietnam", "84"),
			new Country("Wallis and Futuna", "681"),
			new Country("Yemen", "967"), new Country("Zambia", "260"),
			new Country("Zimbabwe", "263") };

}
