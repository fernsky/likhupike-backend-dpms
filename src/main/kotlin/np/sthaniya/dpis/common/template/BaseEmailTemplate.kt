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
            <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        </head>
        <body style="
            margin: 0;
            padding: 0;
            background-color: #ffffff;
            -webkit-font-smoothing: antialiased;
            min-width: 100%;
            width: 100% !important;
            height: 100% !important;
        ">
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" style="min-width: 600px;">
                <tr height="40"><td></td></tr>
                <tr align="center">
                    <td width="100%">
                        <table border="0" cellspacing="0" cellpadding="0" style="
                            padding: 0 20px 20px;
                            width: 100%;
                            max-width: 680px;
                            min-width: 600px;
                        ">
                            <tr>
                                <td>
                                    <div style="
                                        border: thin solid #dadce0;
                                        border-radius: 12px;
                                        padding: 48px 40px;
                                        background-color: #ffffff;
                                        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                                    " align="center">
                                        <div style="
                                            font-family: Roboto, -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
                                            border-bottom: thin solid #dadce0;
                                            color: $GRAY_TEXT;
                                            line-height: 1.5;
                                            padding-bottom: 24px;
                                            text-align: center;
                                            word-break: break-word;
                                        ">
                                            <div style="
                                                font-size: 28px;
                                                font-weight: 500;
                                                letter-spacing: -0.02em;
                                            ">${title}</div>
                                        </div>
                                        
                                        <div style="
                                            font-family: Roboto, -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
                                            font-size: 15px;
                                            color: $GRAY_TEXT;
                                            line-height: 1.6;
                                            padding: 32px 0;
                                            text-align: left;
                                        ">
                                            ${content}
                                        </div>

                                        <div style="
                                            padding-top: 32px;
                                            font-size: 13px;
                                            line-height: 1.5;
                                            color: $LIGHT_GRAY;
                                            letter-spacing: 0.2px;
                                            text-align: center;
                                            border-top: 1px solid #f1f3f4;
                                            font-family: Roboto, -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
                                        ">
                                            <div style="direction: ltr">
                                                <strong style="color: #3c4043">Likhu Pike Rural Municipality</strong><br>
                                                Digital Profile Information System
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr height="40"><td></td></tr>
            </table>
        </body>
        </html>
    """
}
