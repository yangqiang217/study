/*
defines the default precision for all floating point data types in the fragment shader. This is like choosing between float and double in our Java code.
lowp, mediump, and highp(only supported in the fragment shader on some implementations)

Why didn’t we have to do this for the vertex shader?
The vertex shader can also have its default precision changed, but because accuracy is more
important when it comes to a vertex’s position, the OpenGL designers decided to set vertex
shaders to the highest setting, highp, by default.
*/
precision mediump float;
varying vec4 v_Color;

//Unlike an attribute that is set on each vertex, a uniform keeps the same value for all vertices until we change it again
//u_Color is also a four-component vector:rgba
//uniform vec4 u_Color;

void main()
{
    //Our shader must write something to gl_FragColor
    gl_FragColor = v_Color;
}
