package np.sthaniya.dpis.common.template

object BaseEmailTemplate {
    private const val PRIMARY_DARK = "#0b1f42"
    private const val PRIMARY_MAIN = "#123772"
    private const val PRIMARY_LIGHT = "#1a4894"

    fun wrap(title: String, content: String) = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title}</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; line-height: 1.6; color: #546e7a; background: #f5f5f5;">
            <div style="
                background: linear-gradient(165deg, $PRIMARY_MAIN 0%, rgba(18, 55, 114, 0.95) 45%, rgba(10, 21, 38, 0.98) 75%, #0a1526 100%);
                min-height: 100vh;
                padding: 2rem 1rem;
            ">
                <!-- Main Container -->
                <div style="
                    max-width: 600px;
                    margin: 0 auto;
                    background: rgba(255, 255, 255, 0.98);
                    border-radius: 24px;
                    overflow: hidden;
                    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2), 0 5px 15px rgba(0, 0, 0, 0.1);
                    border: 1px solid rgba(255, 255, 255, 0.7);
                ">
                    <!-- Header -->
                    <div style="
                        padding: 1.75rem 2rem 1rem;
                        background: linear-gradient(to bottom, rgba(255, 255, 255, 0.98), rgba(255, 255, 255, 0.95));
                        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
                        text-align: center;
                    ">
                        <h1 style="
                            font-size: 1.5rem;
                            margin: 0 0 0.25rem;
                            color: $PRIMARY_DARK;
                            font-weight: 600;
                            letter-spacing: -0.01em;
                            line-height: 1.1;
                        ">${title}</h1>
                    </div>
                    
                    <!-- Content -->
                    <div style="padding: 2rem;">
                        ${content}
                    </div>

                    <!-- Footer -->
                    <div style="
                        margin-top: 2rem;
                        padding: 1.5rem;
                        background: rgba(0, 0, 0, 0.02);
                        border-top: 1px solid rgba(0, 0, 0, 0.08);
                        text-align: center;
                        font-size: 0.875rem;
                        color: #546e7a;
                    ">
                        <p style="margin: 0;">
                            This is an automated message from Likhu Pike Digital Profile System
                        </p>
                    </div>
                </div>
            </div>
        </body>
        </html>
    """
}
