package org.yankov.finance.manager

import javax.swing.JButton

object Buttons {
  def button(title: String, command: String): JButton = {
    val b = new JButton(title)
    b.setActionCommand(command)
    b
  }
}
