precision mediump float;

//This variable type refers to an array of two-dimensional texture data.
uniform sampler2D u_TextureUnit;

varying vec2 v_TextureCoordinates;
void main()
{
    //texture2D() will read in the color value for the texture at that particular coordinate.
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
}