package np.sthaniya.dpis.common.template

object BaseEmailTemplate {
    private const val PRIMARY_DARK = "#0b1f42"
    private const val PRIMARY_MAIN = "#123772"
    private const val SLATE_600 = "#475569"
    private const val SLATE_700 = "#334155"
    private const val SLATE_800 = "#1e293b"
    
    fun wrap(title: String, content: String) = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title}</title>
        </head>
        <body style="
            margin: 0;
            padding: 0;
            background-color: #f8fafc;
            -webkit-font-smoothing: antialiased;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Helvetica Neue', Arial, sans-serif;
        ">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="
                background-color: $PRIMARY_MAIN;
                min-width: 100%;
            ">
                <tr>
                    <td align="center" style="padding: 60px 10px;">
                        <table width="100%" cellpadding="0" cellspacing="0" border="0" style="max-width: 580px;">
                            <tr>
                                <td align="center" bgcolor="#ffffff" style="
                                    border-radius: 16px;
                                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                                    border: 1px solid #f1f5f9;
                                ">
                                    <!-- Header -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td align="center" style="
                                                padding: 48px 40px 40px;
                                                border-bottom: 1px solid #f1f5f9;
                                                background-color: #ffffff;
                                                border-radius: 16px 16px 0 0;
                                            ">
                                                <h1 style="
                                                    margin: 0;
                                                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
                                                    font-size: 26px;
                                                    line-height: 1.3;
                                                    font-weight: 700;
                                                    color: $SLATE_800;
                                                    letter-spacing: -0.025em;
                                                ">${title}</h1>
                                            </td>
                                        </tr>
                                    </table>
                                    
                                    <!-- Content -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="
                                                padding: 40px 40px 48px;
                                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
                                                font-size: 16px;
                                                line-height: 1.625;
                                                color: $SLATE_700;
                                            ">
                                                ${content}
                                            </td>
                                        </tr>
                                    </table>

                                    <!-- Footer -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td align="center" style="
                                                padding: 32px 40px;
                                                background-color: #f8fafc;
                                                border-top: 1px solid #f1f5f9;
                                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
                                                font-size: 13px;
                                                line-height: 1.5;
                                                color: $SLATE_600;
                                                border-radius: 0 0 16px 16px;
                                            ">
                                                <strong style="color: $SLATE_700;">Likhu Pike Digital Profile System</strong><br>
                                                Digital Profile Information System
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
    """
}
