package np.sthaniya.dpis.common.template

object BaseEmailTemplate {
    private const val PRIMARY_DARK = "#0b1f42"
    private const val PRIMARY_MAIN = "#123772"
    private const val SLATE_600 = "#475569"
    private const val SLATE_700 = "#334155"
    
    fun wrap(title: String, content: String) = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title}</title>
        </head>
        <body style="margin: 0; padding: 0; background-color: #f8fafc;">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="
                background-color: $PRIMARY_MAIN;
                min-width: 100%;
            ">
                <tr>
                    <td align="center" style="padding: 48px 10px;">
                        <table width="100%" cellpadding="0" cellspacing="0" border="0" style="max-width: 600px;">
                            <tr>
                                <td align="center" bgcolor="#ffffff" style="
                                    border-radius: 12px;
                                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                                    border: 1px solid #f1f5f9;
                                ">
                                    <!-- Header -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td align="center" style="
                                                padding: 40px 40px 32px;
                                                border-bottom: 1px solid #f1f5f9;
                                                background-color: #ffffff;
                                                border-radius: 12px 12px 0 0;
                                            ">
                                                <h1 style="
                                                    margin: 0;
                                                    font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                                    font-size: 24px;
                                                    line-height: 32px;
                                                    font-weight: 600;
                                                    color: $PRIMARY_DARK;
                                                    letter-spacing: -0.025em;
                                                ">${title}</h1>
                                            </td>
                                        </tr>
                                    </table>
                                    
                                    <!-- Content -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="
                                                padding: 40px;
                                                font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                                font-size: 16px;
                                                line-height: 24px;
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
                                                font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                                font-size: 14px;
                                                line-height: 20px;
                                                color: $SLATE_600;
                                                border-radius: 0 0 12px 12px;
                                            ">
                                                Likhu Pike Digital Profile System
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
