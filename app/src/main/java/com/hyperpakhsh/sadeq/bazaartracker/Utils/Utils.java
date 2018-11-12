package com.hyperpakhsh.sadeq.bazaartracker.Utils;


public class Utils {
    public static String HtmlBase = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/bootstrap.css\">\n" +
            "    <title>Sales Factor</title>\n" +
            "\n" +
            "    <style>\n" +
            "\n" +
            "        @font-face {\n" +
            "            font-family: sans;\n" +
            "            src: url(fonts/sans.ttf);\n" +
            "        }\n" +
            "\n" +
            "        @font-face {\n" +
            "            font-family: sanslight;\n" +
            "            src: url(fonts/sanslight.ttf);\n" +
            "        }\n" +
            "\n" +
            "        @font-face {\n" +
            "            font-family: sansmedium;\n" +
            "            src: url(fonts/sansmedium.ttf);\n" +
            "        }\n" +
            "\n" +
            "            td {\n" +
            "                text-align: center;\n" +
            "                padding: 2px;\n" +
            "                border: none;\n" +
            "                font-family: sanslight;\n" +
            "            }\n" +
            "\n" +
            "            th {\n" +
            "                text-align: center;\n" +
            "                padding: 8px;\n" +
            "                font-family: sansmedium;\n" +
            "            }\n" +
            "\n" +
            "\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<p></p>\n" +
            "<H2 align=\"center\" style=\"font-family: sans\">باغ گل رضوان</H2>"+
            "<div class=\"container border \">\n" +
            "        <table width=\"750px\" align=\"center\">\n" +
            "            <tr>\n" +
            "                <td>\n" +
            "                    <table class=\"table table-striped\" width=\"100%\">\n" +
            "                        <tr>\n" +
            "                            <th align=\"center\" colspan=\"3\">\n" +
            "                                <H3 align=\"center\" style=\"font-family: sans\">فاکتور فروش</H3>\n" +
            "                            </th>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                                <td>$date</td>\n" +
            "                                <th>نام مشتری : $customer</th>\n" +
            "\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td>\n" +
            "                <table class=\"table-bordered\" width=\"100%\">\n" +
            "                    <thead>\n" +
            "                    <tr align=\"center\">\n" +
            "                        <th>قیمت کل</th>\n" +
            "                        <th>تعداد</th>\n" +
            "                        <th>قیمت واحد</th>\n" +
            "                        <th>محصول</th>\n" +
            "                        <th>ردیف</th>\n" +
            "                     </tr>\n" +
            "                    </thead>\n" +
            "$order"+
            "                </table>\n" +
            "                </td>\n" +
            "            </tr>\n" +
            "            <tr valign=\"bottom\">\n" +
            "                <td>\n" +
            "                    <table class=\"table-bordered\" width=\"50%\">\n" +
            "                        <tr>\n" +
            "                            <td>$total</td>\n" +
            "                            <td>جمع مبلغ </td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>$shipping</td>\n" +
            "                            <td>هزینه حمل </td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>$discount</td>\n" +
            "                            <td>تخفیف </td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <th>$final</th>\n" +
            "                            <th>مبلغ قابل پرداخت </th>\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "<p></p>\n" +
            "    <table class=\"table\" width=\"100%\">\n" +
            "        <tr>\n" +
            "\n" +
            "            <td>$desc</td>\n" +
            "\n" +
            "        </tr>\n" +
            "    </table>"+
            "        </div>\n" +
            "</body>\n" +
            "</html>";

    public static String orderTr = "<tr align=\"center\">\n" +
            "                                 <td>$totalPrice</td> \n" +
            "                                   <td>$quantity</td> \n" +
            "                                 <td>$price</td> \n" +
            "                                  <td>$product</td> \n" +
            "                                  <td>$row</td>\n" +
            "                                </tr>\"";
}
