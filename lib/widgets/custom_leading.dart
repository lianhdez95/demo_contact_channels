import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';


Widget customLeading(Icon icon) {
  return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        icon,
        const SizedBox(width: 20,)
      ]
  );
}