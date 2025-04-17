package np.sthaniya.dpis.profile.institutions.cooperatives.util

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Utility class for working with images.
 */
@Component
class ImageUtils {
    
    private val logger = LoggerFactory.getLogger(javaClass)
    
    /**
     * Creates a thumbnail of the specified image.
     * 
     * @param imageBytes The original image bytes
     * @param maxSize Maximum dimension (width or height) of the thumbnail
     * @return The thumbnail as a byte array
     */
    fun createThumbnail(imageBytes: ByteArray, maxSize: Int = 300): ByteArray {
        logger.debug("Creating thumbnail with max size $maxSize")
        
        try {
            // Read the original image
            val originalImage = ImageIO.read(ByteArrayInputStream(imageBytes))
            if (originalImage == null) {
                logger.warn("Failed to read image for thumbnail creation")
                return imageBytes // Return original if we can't process it
            }
            
            // Calculate dimensions
            val originalWidth = originalImage.width
            val originalHeight = originalImage.height
            
            if (originalWidth <= maxSize && originalHeight <= maxSize) {
                logger.debug("Image is already smaller than thumbnail size, using original")
                return imageBytes // Return original if it's already small enough
            }
            
            // Calculate new dimensions while preserving aspect ratio
            val ratio = originalWidth.toDouble() / originalHeight.toDouble()
            val (newWidth, newHeight) = if (originalWidth > originalHeight) {
                Pair(maxSize, (maxSize / ratio).toInt())
            } else {
                Pair((maxSize * ratio).toInt(), maxSize)
            }
            
            // Create the thumbnail image
            val thumbnailImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
            val graphics = thumbnailImage.createGraphics()
            
            // Set rendering hints for better quality
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            
            // Draw the original image scaled to the thumbnail size
            graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null)
            graphics.dispose()
            
            // Convert the thumbnail to bytes
            val outputStream = ByteArrayOutputStream()
            ImageIO.write(thumbnailImage, "jpeg", outputStream)
            
            logger.debug("Thumbnail created successfully: ${newWidth}x${newHeight}")
            return outputStream.toByteArray()
            
        } catch (ex: Exception) {
            logger.error("Error creating thumbnail", ex)
            return imageBytes // Return original on error
        }
    }
}
