/*
 * Copyright (C) 2017-2019
 *   Vincenzo Gulisano
 *   Dimitris Palyvos-Giannas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 *   Vincenzo Gulisano info@vincenzogulisano.com
 *   Dimitris Palyvos-Giannas palyvos@chalmers.se
 */

package scheduling.thread;

import java.util.BitSet;
import net.openhft.affinity.Affinity;

/**
 * Thread that runs forever (until interrupted).
 */
public class BasicWorkerThread extends MuSPEThread {

  private final Runnable task;
  private final BitSet affinity;

  public BasicWorkerThread(Runnable task) {
    this(task, null);
  }

  public BasicWorkerThread(Runnable task, BitSet affinity) {
    this.task = task;
    this.affinity = affinity;
  }

  @Override
  public void run() {
    if (affinity != null) {
      Affinity.setAffinity(affinity);
    }
    super.run();
  }

  @Override
  public void doRun() {
    task.run();
  }
}
