# StutterFix
This mod modifies the way threads work (priorities and quantity) in Minecraft, with the aim of reducing stutters when generating chunks. Warning, this mod may slow down chunk generation, but reduces stuttering. This mod is made for the client side and is not designed for dedicated servers.

The mod does not affect the micro stuttering problem that occurs periodically due to the garbage collector, I solved my problem (didn't solve it completely, but micro stuttering occurs rarely and not periodically anymore) using -XX:+UseShenandoahGC in jvm arguments, do a search for jvm arguments for minecraft for more information.

I recommend using this mod with the Sodium mod.

This mod does not reduce the stuttering issue on dual-core and single-core processors. At least 8 thread processors and more are recommended. The more threads the processor has, the better.

In Minecraft there is a group of worker threads that are created at startup with quantity n - 1, where n is the number of threads on your processor. This group of threads performs tasks in parallel, such as generating chunks and other tasks.
The problem is that these worker threads overload the CPU, causing stuttering and to solve the problem, a reduction in the number of these worker threads is made.

Here is the list of worker threads according to the number of processor threads:

<table class="table">
  <thead>
    <tr>
      <th scope="col">CPU threads</th>
      <th scope="col">Vanila worker threads</th>
      <th scope="col">worker threads StutterFix mod</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">1</th>
      <td>1</td>
      <td class="text-danger">1</td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>1</td>
      <td class="text-danger">1</td>
    </tr>
    <tr>
      <th scope="row">4</th>
      <td>3</td>
      <td class="text-danger">1</td>
    </tr>
    <tr>
      <th scope="row">8</th>
      <td>7</td>
      <td>3</td>
    </tr>
    <tr>
      <th scope="row">12</th>
      <td>11</td>
      <td>7</td>
    </tr>
    <tr>
      <th scope="row">16</th>
      <td>15</td>
      <td>11</td>
    </tr>
    <tr>
      <th scope="row">20</th>
      <td>19</td>
      <td>15</td>
    </tr>
    <tr>
      <th scope="row">24</th>
      <td>23</td>
      <td>19</td>
    </tr>
    <tr>
      <th scope="row">28</th>
      <td>27</td>
      <td>23</td>
    </tr>
    <tr>
      <th scope="row">32</th>
      <td>31</td>
      <td>27</td>
    </tr>
  </tbody>
</table>

Demonstration (old):
https://youtu.be/AaZ1jAqitQk
