# Mouse Resize Panel

The panel that records when mouse enters and the side it enters from.
Draws the passed component and makes it follow the mouse such that mouse is at the center of the component.
Resizes the component based on the mouse movement: the dimensions are updated by the formula
$$d = \min \left( \dfrac{d_0}{b} + k \cdot \text{dist},\ d_0 \right)$$
where:
<ul>
  <li>$d_0$ - the original dimension of the component</li>
  <li>$b$ - the initial scaling factor</li>
  <li>$k$ - a scaling factor to adjust sensitivity</li>
  <li>$\text{dist}$ - the distance from the mouse to the side of the panel it entered from</li>
</ul>

Supports complex panels: the code includes an example where the panel first performs a complex calculation and then displays the result.
