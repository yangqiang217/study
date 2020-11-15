//This vertex shader will be called once for every single vertex that we’ve defined.

uniform mat4 u_Matrix;

//When it’s called, it will receive the current vertex’s position in the a_Position attribute, which is defined to be a vec4.
//vec4: xyzw(default:0001)
attribute vec4 a_Position;
attribute vec4 a_Color;

//we wanted our colors to vary across the surface of a triangle
//A varying is a special type of variable that blends the values given to it and sends these values to the fragment shader
//Using a varying, we can blend any two colors together
varying vec4 v_Color;

//All it does is copy the position that we’ve defined to the special output variable gl_Position
void main()
{
    v_Color = a_Color;

    /* It also means that our vertex array will no longer be interpreted as normalized device
    coor-dinates but will now be interpreted as existing in a virtual coordinate space,
    as defined by the matrix. The matrix will transform the coordinates from this virtual
    coordinate space back into normalized device coordinates.*/
    gl_Position = u_Matrix * a_Position;
    //mallets size, tell OpenGL how large the points should appear on the screen
    gl_PointSize = 10.0;
}
