package np.sthaniya.dpis.common.template

object BaseEmailTemplate {
    private const val PRIMARY_DARK = "#1f1f1f"
    private const val PRIMARY_MAIN = "#4184F3"
    private const val GRAY_TEXT = "rgba(0, 0, 0, 0.87)"
    private const val LIGHT_GRAY = "rgba(0, 0, 0, 0.54)"
    
    fun wrap(title: String, content: String) = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title}</title>
            <link href="//fonts.googleapis.com/css?family=Google+Sans:400,500,700" rel="stylesheet">
        </head>
        <body style="
            margin: 0;
            padding: 0;
            background-color: #ffffff;
            -webkit-font-smoothing: antialiased;
        ">
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" style="min-width: 348px;">
                <tr height="32"><td></td></tr>
                <tr align="center">
                    <td>
                        <table border="0" cellspacing="0" cellpadding="0" style="padding-bottom: 20px; max-width: 516px; min-width: 220px;">
                            <tr>
                                <td width="8"></td>
                                <td>
                                    <div style="
                                        border: thin solid #dadce0;
                                        border-radius: 8px;
                                        padding: 40px 20px;
                                    " align="center">
                                        <div style="
                                            font-family: 'Google Sans', Roboto, Arial, sans-serif;
                                            border-bottom: thin solid #dadce0;
                                            color: $GRAY_TEXT;
                                            line-height: 32px;
                                            padding-bottom: 24px;
                                            text-align: center;
                                            word-break: break-word;
                                        ">
                                            <div style="font-size: 24px;">${title}</div>
                                        </div>
                                        
                                        <div style="
                                            font-family: Roboto, Arial, sans-serif;
                                            font-size: 14px;
                                            color: $GRAY_TEXT;
                                            line-height: 20px;
                                            padding-top: 20px;
                                            text-align: left;
                                        ">
                                            ${content}
                                        </div>

                                        <div style="
                                            padding-top: 20px;
                                            font-size: 12px;
                                            line-height: 16px;
                                            color: $LIGHT_GRAY;
                                            letter-spacing: 0.3px;
                                            text-align: center;
                                            border-top: 1px solid #dadce0;
                                            margin-top: 20px;
                                            padding-top: 20px;
                                        ">
                                            <div style="direction: ltr">
                                                Likhu Pike Rural Municipality<br>
                                                Digital Profile Information System
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td width="8"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="32"><td></td></tr>
            </table>
        </body>
        </html>
    """
}
