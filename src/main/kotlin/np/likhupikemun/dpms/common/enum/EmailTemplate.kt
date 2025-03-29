package np.likhupikemun.dpis.common.enum

enum class EmailTemplate(val subject: String, val template: String) {
    PASSWORD_RESET(
        subject = "Reset Your Password - Likhu Pike Digital Profile System",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Reset Your Password</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Password Reset Request</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>We received a request to reset your password. Click the button below to reset it:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%resetLink%" style="background-color: #3498db; color: white; padding: 12px 25px; text-decoration: none; border-radius: 3px;">Reset Password</a>
                        </div>
                        <p>Or copy and paste this link in your browser:</p>
                        <p style="background-color: #eee; padding: 10px; border-radius: 3px;">%resetLink%</p>
                        <p>This link will expire in 15 minutes.</p>
                        <p>If you didn't request this, please ignore this email.</p>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    ),

    WELCOME(
        subject = "Welcome to Likhu Pike Digital Profile System",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Welcome to dpis</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Welcome to dpis</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>Welcome to the Likhu Pike Digital Profile Information System. Your account has been created successfully and is pending approval.</p>
                        <p>Once your account is approved, you will receive another email with login instructions.</p>
                        <div style="margin: 30px 0; padding: 15px; background-color: #e8f4f8; border-radius: 5px;">
                            <p style="margin: 0;"><strong>Email:</strong> %email%</p>
                        </div>
                        <p>If you have any questions, please contact our support team.</p>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    ),

    ACCOUNT_APPROVED(
        subject = "Your Account Has Been Approved - Likhu Pike dpis",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Account Approved</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Account Approved</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>Great news! Your account has been approved and is now ready to use.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%loginLink%" style="background-color: #27ae60; color: white; padding: 12px 25px; text-decoration: none; border-radius: 3px;">Login Now</a>
                        </div>
                        <div style="margin: 30px 0; padding: 15px; background-color: #e8f4f8; border-radius: 5px;">
                            <p style="margin: 0;"><strong>Email:</strong> %email%</p>
                        </div>
                        <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    ),

    ACCOUNT_CREATED(
        subject = "Your Account Has Been Created - Likhu Pike dpis",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Account Created</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Account Created</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>An account has been created for you in the Likhu Pike Digital Profile Information System.</p>
                        <p>For security reasons, you need to set your own password before you can access your account.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%resetLink%" style="background-color: #e74c3c; color: white; padding: 12px 25px; text-decoration: none; border-radius: 3px;">Set Your Password</a>
                        </div>
                        <div style="margin: 30px 0; padding: 15px; background-color: #e8f4f8; border-radius: 5px;">
                            <p style="margin: 0;"><strong>Email:</strong> %email%</p>
                        </div>
                        <p>If you did not expect this account creation, please contact our support team immediately.</p>
                        <p><strong>Note:</strong> This link will expire in 24 hours for security reasons.</p>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    ),

    PASSWORD_RESET_OTP(
        subject = "Your Password Reset OTP - Likhu Pike dpis",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset OTP</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Password Reset Code</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>You have requested to reset your password. Here is your one-time password (OTP):</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <div style="background: #e8f4f8; padding: 15px; border-radius: 5px; font-size: 24px; letter-spacing: 5px; font-weight: bold;">
                                %otp%
                            </div>
                        </div>
                        <p><strong>This code will expire in 15 minutes.</strong></p>
                        <p>If you did not request this password reset, please ignore this email or contact support if you have concerns.</p>
                        <div style="margin-top: 20px; padding: 15px; background-color: #fff3cd; border-radius: 5px; color: #856404;">
                            <p style="margin: 0;"><strong>Security Tips:</strong></p>
                            <ul style="margin: 10px 0 0 0;">
                                <li>Never share this OTP with anyone</li>
                                <li>Our staff will never ask for your OTP</li>
                                <li>Make sure you're on our official website</li>
                            </ul>
                        </div>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    ),

    PASSWORD_RESET_SUCCESS(
        subject = "Password Reset Successful - Likhu Pike dpis",
        template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset Successful</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #2c3e50;">Password Reset Successful</h1>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p>Hello,</p>
                        <p>Your password has been successfully reset.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%loginLink%" style="background-color: #27ae60; color: white; padding: 12px 25px; text-decoration: none; border-radius: 3px;">Login Now</a>
                        </div>
                        <div style="margin-top: 20px; padding: 15px; background-color: #fff3cd; border-radius: 5px; color: #856404;">
                            <p style="margin: 0;"><strong>Important:</strong> If you did not perform this password reset, please contact our support team immediately.</p>
                        </div>
                    </div>
                    <div style="margin-top: 30px; text-align: center; color: #666;">
                        <p>This is an automated message from Likhu Pike Digital Profile System</p>
                    </div>
                </div>
            </body>
            </html>
        """
    )
}
