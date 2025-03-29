package np.sthaniya.dpis.common.template

object BaseEmailTemplate {
    private const val PRIMARY_DARK = "#0b1f42"
    private const val PRIMARY_MAIN = "#123772"
    
    fun wrap(title: String, content: String) = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title}</title>
        </head>
        <body style="margin: 0; padding: 0; background-color: #f5f5f5;">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color: #f5f5f5;">
                <tr>
                    <td align="center" style="padding: 40px 10px;">
                        <table width="100%" cellpadding="0" cellspacing="0" border="0" style="max-width: 600px;">
                            <tr>
                                <td align="center" bgcolor="#ffffff" style="
                                    border-radius: 8px;
                                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                                ">
                                    <!-- Header -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td align="center" style="
                                                padding: 30px 30px 20px;
                                                border-bottom: 1px solid #edf2f7;
                                            ">
                                                <h1 style="
                                                    margin: 0;
                                                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                                                    font-size: 24px;
                                                    line-height: 32px;
                                                    font-weight: 600;
                                                    color: $PRIMARY_DARK;
                                                ">${title}</h1>
                                            </td>
                                        </tr>
                                    </table>
                                    
                                    <!-- Content -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="
                                                padding: 32px 30px;
                                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                                                font-size: 16px;
                                                line-height: 24px;
                                                color: #333333;
                                            ">
                                                ${content}
                                            </td>
                                        </tr>
                                    </table>

                                    <!-- Footer -->
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td align="center" style="
                                                padding: 24px 30px;
                                                background-color: #f8fafc;
                                                border-top: 1px solid #edf2f7;
                                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                                                font-size: 14px;
                                                line-height: 20px;
                                                color: #64748b;
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
