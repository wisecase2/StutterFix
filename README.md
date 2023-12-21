# StutterFix
StutterFix is a mod that aims to reduce or even eliminate stuttering and lag spikes related to exploring new areas of the world, which is where chunks are generated.
This mod is made for singleplayer, where you play with an integrated server (yes, singleplayer minecraft uses an integrated server). This mod may slow down chunk generation depending on how much CPU usage Minecraft used on average. If you have a modern processor and are using the default chunk simulation (not render distance) of 12, you probably won't see a difference in chunk generation, as Minecraft uses less CPU on average and that's why it tends to have less stuttering, but it can still happen, because in some rare moments it can reach 100% cpu usage.

The mod does not affect the micro stuttering problem that occurs periodically due to the garbage collector, I solved my problem (didn't solve it completely, but micro stuttering occurs rarely and not periodically anymore) using -XX:+UseShenandoahGC in jvm arguments, do a search for jvm arguments for minecraft for more information.

This mod does not reduce the stuttering issue on dual-core (non-hyperthreading) and single-core processors.

<h1>Other mods I recommend</h1>

I recommend using this mod with the Sodium mod and Concurrent Chunk Management Engine (C2ME).

<h1>How it works?</h1>

In Minecraft there is a group of worker threads that are created at startup with quantity n - 1, where n is the number of threads on your processor. This group of threads performs tasks in parallel, such as generating chunks and other tasks.

The problem is that these worker threads overload the CPU, causing stutters, lag spikes and to solve the problem, a reduction in the number of these worker threads is made.

Here is the list of worker threads according to the number of processor threads:

<table class="table">
  <thead>
    <tr>
      <th scope="col">CPU threads</th>
      <th scope="col">Vanilla</th>
      <th scope="col">StutterFix mod</th>
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

In versions 0.1.5 and earlier, all these worker threads have minimum priority, due to the low responsiveness in chunk generation I decided to launch 0.1.6 with the worker threads at normal priority. In version 0.1.7 I had the idea of leaving the first half of the worker threads with normal priority and the last half with minimum priority to better reduce stuttering and better responsiveness when generating chunks.

Demonstration (old):
https://youtu.be/AaZ1jAqitQk
