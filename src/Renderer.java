/**
 * Created by Jaco on 4/19/16.
 *
 * I was thinking this class could handle all the culling/clipping operations
 * while the Camera class would handle the transformations and projections
 *
 * Assumes Camera is at
 *
 * 1. Cull away triangles facing away from the camera
 * 2. Map to NDC
 * 3. Clip into cube (3D Cohen-Sutherland)
 * 4. Return to camera to project
 */
public class Renderer {

}
